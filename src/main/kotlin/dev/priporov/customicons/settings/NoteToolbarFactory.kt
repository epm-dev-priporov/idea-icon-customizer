package dev.priporov.customicons.settings

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import javax.swing.JPanel

object NoteToolbarFactory {

    fun getInstance(list: JBList<Any?>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)

        return decorator.createPanel()
    }

}
