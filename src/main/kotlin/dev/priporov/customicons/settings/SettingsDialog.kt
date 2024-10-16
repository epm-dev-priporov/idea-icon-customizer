package dev.priporov.customicons.settings

import com.intellij.ui.CollectionListModel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.DefaultListCellRenderer
import javax.swing.JButton
import javax.swing.JPanel


class SettingsDialog(private val iconConfigurable: IconConfigurable) {

    var root: JPanel = JPanel()
    private var button: JButton = JButton("test")

    private var patternPanel: MutableList<JPanel> = ArrayList()
    private val list = JBList<Any?>(CollectionListModel(patternPanel))
//    private val list = JBList<Any?>(ListComboBoxModel(patternPanel))
    init {
        button.apply {
            isVisible = true
        }

        val jbScrollPane = JBScrollPane()
        jbScrollPane.setViewportView(list)

        val createScrollPane = ScrollPaneFactory.createScrollPane(
            jbScrollPane,
            SideBorder.TOP
        )
        val data: MutableList<String> = ArrayList()
        data.add("Item 1")
        data.add("Item 2")
        data.add("Item 3")
        data.add("Item 4")
        data.add("Item 5")
        list.setListData(data.toTypedArray())

//        val toolbarPanel = NoteToolbarFactory.getInstance(list)

        list.setCellRenderer(CustomJBListCellRenderer())

        root.layout = BorderLayout().apply {
//            addLayoutComponent(toolbarPanel, "North")
            addLayoutComponent(jbScrollPane, "West")
            addLayoutComponent(list, "West")
            addLayoutComponent(button, "East")
        }
        root.isVisible = true
        root.add(jbScrollPane)
        root.add(list)
        root.add(button)
//        root.add(createScrollPane)
    }

}

class CustomJBListCellRenderer: DefaultListCellRenderer(){

}