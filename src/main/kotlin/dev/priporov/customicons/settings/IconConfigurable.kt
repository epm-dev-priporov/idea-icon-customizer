package dev.priporov.customicons.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

class IconConfigurable: SearchableConfigurable {

    private var state = false

    private val gui: SettingsDialog by lazy {
        service<SettingsDialog>()
    }

    override fun createComponent(): JComponent = gui.root

    override fun isModified(): Boolean = state

    override fun apply() {}

    override fun getDisplayName(): String = "Icon Customizer"

    override fun getId(): String = "dev.priporov.customicons.settings.IconConfigurable"
}