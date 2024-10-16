package dev.priporov.customicons.settings

import com.intellij.ui.ToolbarDecorator
import javax.swing.JList
import javax.swing.JPanel

object NoteToolbarFactory {

    fun getInstance(list: JList<*>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)

        return decorator.createPanel()
    }

}
