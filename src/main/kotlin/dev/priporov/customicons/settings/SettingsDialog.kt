package dev.priporov.customicons.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBList
import dev.priporov.customicons.pattern.item.*
import dev.priporov.customicons.pattern.panel.PatternPanel
import dev.priporov.customicons.service.SettingsListModelService
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*


@Service
class SettingsDialog {

    var root: JPanel = JPanel()

    private val list = JBList(CollectionListModel(ArrayList<BaseConditionItem>()))
    private val patternPanel = PatternPanel()
    private val patternEditingPanel = patternPanel.root

    init {
        val toolbarPanel = PatternItemsToolbarFactory.getInstance(list)

        val model = service<SettingsListModelService>()

        list.apply {
            setModel(model)
            setCellRenderer(CustomListCellRenderer())
        }

        list.addListSelectionListener {
            val selectedValue = (it.source as JList<*>).selectedValue
            if(selectedValue != null){
                patternPanel.showSelectedItem(selectedValue as BaseConditionItem)
            }
        }

        root.apply {
            layout = GridLayout(1,2).apply {
                add(toolbarPanel)
                add(patternEditingPanel)
            }
            isVisible = true
            add(toolbarPanel)
            add(patternEditingPanel)
        }
    }

    fun hidePatternPanel(){
        patternPanel.root.isVisible = false
    }

    fun repaintPatternPanel(){
        val selectedValue = list.selectedValue
        if(selectedValue != null){
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
            if(item.iconContainer != null) {
                setIcon(item.iconContainer?.icon)
            }
        }
    }
}