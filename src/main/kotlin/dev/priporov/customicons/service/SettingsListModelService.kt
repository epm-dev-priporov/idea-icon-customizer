package dev.priporov.customicons.service

import com.intellij.openapi.components.*
import dev.priporov.customicons.icon.IconImporter
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.settings.SettingsDialog
import dev.priporov.customicons.state.Icon
import dev.priporov.customicons.state.PluginState
import javax.swing.DefaultListModel

@Service
@State(
    name = "dev.priporov.customicons.service.SettingsListModelService",
    storages = [Storage("icon-customizer.xml")]
)
class SettingsListModelService : DefaultListModel<BaseConditionItem>(), PersistentStateComponent<PluginState> {

    private var state = PluginState()

    override fun addElement(element: BaseConditionItem) {
        super.addElement(element)
        state.addItemInfo(element)
    }

    override fun removeElement(item: Any?): Boolean {
        val result = if (item != null && super.contains(item) && item is BaseConditionItem) {
            state.removeInfo(item)
            super.removeElement(item)
        } else false
        if (size == 0) {
            service<SettingsDialog>().hidePatternPanel()
        }
        return result
    }

    fun reload() {
        fireContentsChanged(this, 0, size)
    }

    fun getItems() = elements().asSequence().toList()

    override fun getState(): PluginState {
        return state
    }

    override fun loadState(loadedState: PluginState) {
        state = loadedState
        state.getItems().forEach { addElement(it) }

        service<IconImporter>().import(state.versionIcon)

        state.versionIcon = Icon.VERSION
    }

}