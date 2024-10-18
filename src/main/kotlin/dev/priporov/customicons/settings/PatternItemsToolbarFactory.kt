package dev.priporov.customicons.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.customicons.pattern.panel.PatternPanel
import dev.priporov.customicons.service.SettingsListModelService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel

object PatternItemsToolbarFactory {

    fun getInstance(list: JList<*>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)
        decorator.setAddAction(ToolbarAction {
            OkDialog("Create new pattern").show()
        })
        decorator.setRemoveAction(ToolbarAction {
            val selectedValue = list.selectedValue
            if(selectedValue != null) {
               service<SettingsListModelService>().removeElement(selectedValue)
            }
        })

        return decorator.createPanel()
    }

}

class ToolbarAction(private val function: () -> Unit) : AnActionButtonRunnable {
    override fun run(t: AnActionButton) = function.invoke()
}

class OkDialog(
    title: String
) : DialogWrapper(true) {

    private val patternPanel = PatternPanel().apply {
        root.isVisible = true
        setCancelAction(DialogCloseAction(this@OkDialog))
    }

    init {
        setCrossClosesWindow(true)

        init()
        this.title = title
        getButton(okAction)?.setVisible(false)
        getButton(cancelAction)?.setVisible(false)
    }

    override fun createCenterPanel(): JComponent = patternPanel.root

    class DialogCloseAction(private val dialog:OkDialog): ActionListener {

        override fun actionPerformed(e: ActionEvent?) {
            dialog.close(CLOSE_EXIT_CODE)
        }

    }
}