package dev.priporov.customicons.pattern.panel

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.customicons.icon.ICON_DIR
import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType
import dev.priporov.customicons.pattern.item.*
import dev.priporov.customicons.service.SettingsListModelService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

private const val SIZE_LIMIT = 24

class PatternPanel {
    lateinit var root: JPanel

    private object State {
        val actionListeners = ArrayList<ActionListener>()
        val documentListener = ArrayList<DocumentListener>()
    }

    private lateinit var conditionField: JTextField
    private lateinit var disabledCheckBox: JCheckBox
    private lateinit var conditionTypeBox: JComboBox<ConditionType>
    private lateinit var fileTypeBox: JComboBox<FileType>
    private lateinit var applyButton: JButton
    private lateinit var cancelButton: JButton
    private lateinit var saveButton: JButton
    private lateinit var iconButton: JButton
    private lateinit var imageIconPanel: JPanel

    private var selectedIcon: IconContainer? = null
    private val conditionFieldEditingAction = ConditionFieldEditingAction(this, saveButton, conditionField.text)

    init {
        ConditionType.entries.forEach { type ->
            conditionTypeBox.addItem(type)
        }
        FileType.entries.forEach { fileType -> fileTypeBox.addItem(fileType) }
        root.isVisible = false
        applyButton.isVisible = false
        cancelButton.isVisible = true
        saveButton.isVisible = true
        saveButton.isEnabled = false

        saveButton.addActionListener(SaveNewPatternAction(this, conditionFieldEditingAction))
        iconButton.addActionListener(ChooseIconAction(this))

        conditionField.document.addDocumentListener(conditionFieldEditingAction)
    }

    fun showSelectedItem(item: BaseConditionItem) {
        iconButton.setIcon(item.iconContainer?.icon)

        conditionField.text = item.condition

        conditionTypeBox.selectedItem = item.conditionType
        fileTypeBox.selectedItem = item.fileType

        imageIconPanel.isVisible
        disabledCheckBox.isSelected = item.disabled
        root.isVisible = true
        cancelButton.isVisible = false
        saveButton.isVisible = false
        applyButton.isVisible = true
        applyButton.isEnabled = false

        val conditionFieldEditingAction = ConditionFieldEditingAction(this, applyButton, item.condition)
        State.documentListener.apply {
            forEach { conditionField.document.removeDocumentListener(it) }
            clear()
            add(conditionFieldEditingAction)
        }

        initListeners(item, conditionFieldEditingAction)
    }

    private fun initListeners(
        item: BaseConditionItem,
        conditionFieldEditingAction: ConditionFieldEditingAction
    ) {
        val fileTypeAction = FileTypeAction(this, item)
        val conditionTypeAction = ConditionTypeAction(this, item)
        val editNewPatternAction = EditNewPatternAction(this, item)
        val checkboxIconAction = CheckboxIconAction(this, item)
        State.actionListeners.apply {
            forEach {
                applyButton.removeActionListener(it)
                disabledCheckBox.removeActionListener(it)
                conditionTypeBox.removeActionListener(it)
                fileTypeBox.removeActionListener(it)
            }
            clear()
            add(editNewPatternAction)
            add(checkboxIconAction)
            add(conditionTypeAction)
            add(fileTypeAction)
        }

        conditionField.document.addDocumentListener(conditionFieldEditingAction)
        disabledCheckBox.addActionListener(checkboxIconAction)
        applyButton.addActionListener(editNewPatternAction)
        conditionTypeBox.addActionListener(conditionTypeAction)
        fileTypeBox.addActionListener(fileTypeAction)
    }

    fun setCancelAction(action: ActionListener) {
        cancelButton.addActionListener(action)
    }

    private fun invokeCancelAction() = cancelButton.doClick()

    private fun newItem(): BaseConditionItem {
        val fileType: FileType = fileTypeBox.selectedItem as FileType
        val conditionType = conditionTypeBox.selectedItem as ConditionType
        val condition = conditionField.text

        val item = when (conditionType) {
            ConditionType.REGEXP -> RegexpPatternItem(condition)
            ConditionType.EQUALS -> EqualsPatternItem(condition)
            ConditionType.CONTAINS -> ContainsPatternItem(condition)
            ConditionType.EXTENSION -> ExtensionEqualsPatternItem(condition)
            ConditionType.ENDS_WITH -> EndsWithPatternItem(condition)
            ConditionType.STARTS_WITH -> StartsWithPatternItem(condition)
        }
        item.fileType = fileType
        item.iconContainer = selectedIcon

        return item
    }

    private class SaveNewPatternAction(
        private val patternPanel: PatternPanel,
        private val editingAction: ConditionFieldEditingAction
    ) : ActionListener {

        override fun actionPerformed(e: ActionEvent?) {
            val item = patternPanel.newItem()
            service<SettingsListModelService>().apply {
                addElement(item)
            }

            patternPanel.root
            patternPanel.conditionField.document.removeDocumentListener(editingAction)

            updateProjectViewStructure()

            patternPanel.invokeCancelAction()
        }

    }

    private class EditNewPatternAction(
        private val patternPanel: PatternPanel,
        private val item: BaseConditionItem
    ) : ActionListener {

        override fun actionPerformed(e: ActionEvent?) {
            item.condition = patternPanel.conditionField.text
            item.fileType = patternPanel.fileTypeBox.selectedItem as FileType
            item.conditionType = patternPanel.conditionTypeBox.selectedItem as ConditionType
            item.disabled = patternPanel.disabledCheckBox.isSelected
            item.iconContainer = patternPanel.selectedIcon

            service<SettingsListModelService>().updateElement(item)
            service<SettingsListModelService>().reload()

            patternPanel.applyButton.isEnabled = false

            updateProjectViewStructure()
        }
    }

    private class ConditionFieldEditingAction(
        private val panel: PatternPanel,
        private val button: JButton,
        private val itemCondition: String
    ) : DocumentListener {

        override fun insertUpdate(e: DocumentEvent?) {
            button.isEnabled = (hasTextDifference())
        }

        override fun removeUpdate(e: DocumentEvent?) {
            button.isEnabled = hasTextDifference() && panel.conditionField.text.isNotEmpty()
        }

        override fun changedUpdate(e: DocumentEvent?) {
            button.isEnabled = hasTextDifference()
        }

        private fun hasTextDifference() = (panel.conditionField.text != itemCondition)
    }

    private class ChooseIconAction(
        private val patternPanel: PatternPanel,
    ) : ActionListener {
        private val extensions = setOf("png", "jpg", "jpeg")

        override fun actionPerformed(e: ActionEvent?) {
            val descriptor = fileChooserDescriptor()
            FileChooser.chooseFile(descriptor, null, createVirtualFile(File(ICON_DIR)))?.also { file ->
                patternPanel.apply {
                    if (conditionField.text.isNotEmpty()) {
                        applyButton.isEnabled = true
                    }
                    selectedIcon = IconContainer(ImageIcon(file.path), file.path)
                    iconButton.setIcon(selectedIcon?.icon)
                }

            }
        }

        private fun fileChooserDescriptor() = FileChooserDescriptor(
            true,
            false,
            true,
            true,
            false,
            false
        ).apply {
            withFileFilter { file ->
                val isImage = extensions.contains(file.extension)
                if (!isImage) {
                    return@withFileFilter false
                }
                if (file.length == 0L || file.length > 3000) {
                    return@withFileFilter false
                }

                val read: BufferedImage = ImageIO.read(file.inputStream)
                if (read.width > SIZE_LIMIT || read.height > SIZE_LIMIT) {
                    return@withFileFilter false
                }
                return@withFileFilter true
            }
        }
    }

    private class CheckboxIconAction(
        private val patternPanel: PatternPanel,
        private val item: BaseConditionItem
    ) : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            patternPanel.applyButton.isEnabled = (patternPanel.disabledCheckBox.isSelected != item.disabled)
        }
    }

    private class ConditionTypeAction(
        private val patternPanel: PatternPanel,
        private val item: BaseConditionItem
    ) : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            patternPanel.applyButton.isEnabled = patternPanel.conditionTypeBox.selectedItem != item.conditionType
        }
    }

    private class FileTypeAction(
        private val patternPanel: PatternPanel,
        private val item: BaseConditionItem
    ) : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            patternPanel.applyButton.isEnabled = patternPanel.fileTypeBox.selectedItem != item.fileType
        }
    }

}

fun updateProjectViewStructure() {
    ProjectManager.getInstance().openProjects.forEach { project ->
        ProjectView.getInstance(project)?.refresh()
    }
}

private fun createVirtualFile(file: File): VirtualFile? {
    val localFileSystem = LocalFileSystem
        .getInstance()
    val path = file.toPath()

    return localFileSystem.refreshAndFindFileByNioFile(path)
        ?: localFileSystem.refreshAndFindFileByIoFile(file)
        ?: localFileSystem.refreshAndFindFileByPath(path.toString())
}