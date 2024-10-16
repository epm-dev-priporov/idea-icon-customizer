package dev.priporov.customicons.settings

import com.intellij.ui.components.JBTextField
import javax.swing.BoxLayout
import javax.swing.JPanel

class PatternEditingPanel: JPanel() {
    private val textArea = JBTextField()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS).apply {
            add(textArea)
        }
        add(textArea)
        isVisible = true
    }

}