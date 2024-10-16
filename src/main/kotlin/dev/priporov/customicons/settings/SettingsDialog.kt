package dev.priporov.customicons.settings

import com.intellij.ui.CollectionListModel
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.ExpandedItemListCellRendererWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import dev.priporov.customicons.pattern.RegexpPatternItem
import dev.priporov.customicons.pattern.panel.PatternPanel
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer


class SettingsDialog(private val iconConfigurable: IconConfigurable) {

    var root: JPanel = JPanel()

    private var patternPanel: MutableList<RegexpPatternItem> = ArrayList()
    private val list = JBList<Any?>(CollectionListModel(patternPanel))
    private val patternEditingPanel = PatternPanel().root

    init {
        val toolbarPanel = NoteToolbarFactory.getInstance(list)

        val data: MutableList<RegexpPatternItem> = patternPanel
        for (i in 0..50) {
            data.add(RegexpPatternItem("Item LONG LONG LONG  $i"))
        }
        list.setListData(data.toTypedArray())

//        val rendererWrapper: ExpandedItemListCellRendererWrapper<Any> = ExpandedItemListCellRendererWrapper(
//            DefaultListCellRenderer(),
//            CompletionExtender(list)
//        )
//        list.setCellRenderer(CustomListCellRenderer(textArea, rendererWrapper))
        val gridBag = GridBagConstraints()
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

    class CustomListCellRenderer(
        private val textArea: JBTextField,
        private val rendererWrapper: ExpandedItemListCellRendererWrapper<Any>
    ) : ListCellRenderer<Any?>, ColoredListCellRenderer<Any?>() {

        override fun getListCellRendererComponent(
            list: JList<out Any?>?,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            if (list != null) {
                customizeCellRenderer(list, value, index, isSelected, cellHasFocus)
            }
            if (list?.selectedValue != null) {
                textArea.isVisible = true
            }
            return rendererWrapper.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        }

        override fun customizeCellRenderer(
            list: JList<out Any?>,
            value: Any?,
            index: Int,
            selected: Boolean,
            hasFocus: Boolean
        ) {
            println()

        }

    }
}