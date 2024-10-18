package dev.priporov.customicons.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.settings.SettingsDialog
import javax.swing.DefaultListModel

@Service
class SettingsListModelService : DefaultListModel<BaseConditionItem>() {

    override fun addElement(element: BaseConditionItem?) {
        super.addElement(element)
    }

    override fun removeElement(obj: Any?): Boolean {
        val result = if (super.contains(obj)) {
            super.removeElement(obj)
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

}