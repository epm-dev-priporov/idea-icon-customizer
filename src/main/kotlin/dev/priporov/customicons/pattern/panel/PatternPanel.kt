package dev.priporov.customicons.pattern.panel

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.ProjectManager
import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType
import dev.priporov.customicons.pattern.item.*
import dev.priporov.customicons.service.SettingsListModelService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class PatternPanel {
    lateinit var root: JPanel
    private object State{
         val editNewPatternActions = ArrayList<EditNewPatternAction>()
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

    private var selectedIcon: ImageIcon? = null
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

        saveButton.addActionListener(CreateNewPatternAction(this, conditionFieldEditingAction))
        iconButton.addActionListener(ChooseIconAction(this))

        conditionField.document.addDocumentListener(conditionFieldEditingAction)
    }

    fun showSelectedItem(item: BaseConditionItem) {
        iconButton.setIcon(item.icon)

        conditionField.text = item.condition
        conditionField.document.addDocumentListener(ConditionFieldEditingAction(this, applyButton, item.condition))

        conditionTypeBox.selectedItem = item.conditionType
        fileTypeBox.selectedItem = item.fileType

        imageIconPanel.isVisible

        root.isVisible = true
        cancelButton.isVisible = false
        saveButton.isVisible = false
        applyButton.isVisible = true
        applyButton.isEnabled = false

        val editNewPatternAction = EditNewPatternAction(this, item)
        State.editNewPatternActions.apply {
            forEach { applyButton.removeActionListener(it) }
            clear()
            add(editNewPatternAction)
        }

        applyButton.addActionListener(editNewPatternAction)
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
            ConditionType.EXTENSION_EQUALS -> ExtensionEqualsPatternItem(condition)
        }
        item.fileType = fileType
        item.icon = selectedIcon

        return item
    }

    private class CreateNewPatternAction(
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
            item.active = !patternPanel.disabledCheckBox.isSelected
            item.icon = patternPanel.selectedIcon

            service<SettingsListModelService>().reload()

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

        private fun hasTextDifference() = itemCondition != panel.conditionField.text
    }

    private class ChooseIconAction(
        private val patternPanel: PatternPanel,
    ) : ActionListener {
        private val extensions = setOf("png", "jpg", "jpeg", "png")

        override fun actionPerformed(e: ActionEvent?) {
            val descriptor = fileChooserDescriptor()
            FileChooser.chooseFile(descriptor, null, null)?.also { file ->
                patternPanel.apply {
                    if (conditionField.text.isNotEmpty()) {
                        applyButton.isEnabled = true
                    }
                    selectedIcon = ImageIcon(file.path)
                    iconButton.setIcon(selectedIcon)
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

                val read: BufferedImage = ImageIO.read(file.inputStream)
                if (read.width > 24 || read.height > 24) {
                    return@withFileFilter false
                }
                return@withFileFilter true
            }
        }
    }

}

private fun updateProjectViewStructure() {
    ProjectManager.getInstance().openProjects.forEach { project ->
        ProjectView.getInstance(project)?.refresh()
    }
}