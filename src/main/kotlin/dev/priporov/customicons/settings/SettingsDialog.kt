package dev.priporov.customicons.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBList
import com.intellij.ui.util.maximumHeight
import com.intellij.ui.util.preferredWidth
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.pattern.panel.PatternPanel
import dev.priporov.customicons.service.SettingsListModelService
import java.awt.Component
import java.awt.Dimension
import javax.swing.*


@Service
class SettingsDialog {

    var root: JPanel = JPanel()

    private val list = JBList(CollectionListModel(ArrayList<BaseConditionItem>()))
    private val patternPanel = PatternPanel()
    private val patternEditingPanel = patternPanel.root.apply {
        maximumSize = Dimension(preferredWidth, Int.MAX_VALUE)
    }

    init {
        val toolbarPanel = PatternItemsToolbarFactory.getInstance(list).apply {
            maximumSize = Dimension(preferredWidth + 75, root.maximumHeight)
        }

        val model = service<SettingsListModelService>()

        list.apply {
            setModel(model)
            setCellRenderer(CustomListCellRenderer())
        }

        list.addListSelectionListener {
            val selectedValue = (it.source as JList<*>).selectedValue
            if (selectedValue != null) {
                patternPanel.showSelectedItem(selectedValue as BaseConditionItem)
            }
        }

        root.apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS).apply {
                add(toolbarPanel)
                add(patternEditingPanel)
            }
            isVisible = true
        }
    }

    fun hidePatternPanel() {
        patternPanel.root.isVisible = false
    }

    fun repaintPatternPanel() {
        val selectedValue = list.selectedValue
        if (selectedValue != null) {
            patternPanel.showSelectedItem(selectedValue)
        }
    }

}

private class CustomListCellRenderer() : DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {

        return (super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel).apply {
            val item = value as BaseConditionItem
            if (item.iconContainer != null) {
                setIcon(item.iconContainer?.icon)
            }
        }
    }
}