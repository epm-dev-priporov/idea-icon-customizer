package dev.priporov.customicons.settings

import com.intellij.codeInsight.lookup.impl.CompletionExtender
import com.intellij.ui.*
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import dev.priporov.customicons.pattern.RegexpPatternItem
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridLayout
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer


class SettingsDialog(private val iconConfigurable: IconConfigurable) {

    var root: JPanel = JPanel()

    private var patternPanel: MutableList<RegexpPatternItem> = ArrayList()
    private val list = JBList<Any?>(CollectionListModel(patternPanel))
    private val textArea = JBTextField()

    init {
        val jbScrollPane = JBScrollPane()
        jbScrollPane.setViewportView(list)

        val createScrollPane = ScrollPaneFactory.createScrollPane(
            jbScrollPane,
            SideBorder.TOP
        )
        val data: MutableList<RegexpPatternItem> = patternPanel
        for (i in 0..50) {
            data.add(RegexpPatternItem("Item $i"))
        }
        list.setListData(data.toTypedArray())

        val rendererWrapper: ExpandedItemListCellRendererWrapper<Any> = ExpandedItemListCellRendererWrapper(
            DefaultListCellRenderer(),
            CompletionExtender(list)
        )

        list.setCellRenderer(CustomListCellRenderer(textArea, rendererWrapper))

        root.apply {
            layout = GridLayout(3,2).apply {
                addLayoutComponent("123", createScrollPane)
                addLayoutComponent("123", list)
                addLayoutComponent("123", textArea)
                addLayoutComponent("123", JPanel())
            }
            isVisible = true
            add(createScrollPane)
            add(list)
            add(textArea)
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