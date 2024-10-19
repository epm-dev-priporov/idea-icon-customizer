package dev.priporov.customicons.state

import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.pattern.item.IconContainer
import javax.swing.ImageIcon

class PluginState {

    var requiredImport = true;

    var items = HashMap<String, ConditionItemInfo>()

    fun addItemInfo(item: BaseConditionItem) {
        val info = ConditionItemInfo(
            item.condition,
            item.conditionType,
            item.id,
            item.fileType,
            item.iconContainer?.iconPath,
            item.disabled
        )

        items[item.id] = info
    }

    fun removeInfo(item: BaseConditionItem) {
        items.remove(item.id)
    }

    fun getItems(): List<BaseConditionItem> {
        return items.values
            .asSequence()
            .map {toItem(it)}
            .toList()
    }

    private fun toItem(info: ConditionItemInfo): BaseConditionItem {
        return BaseConditionItem(info.condition!!, info.conditionType!!).apply {
            id = info.id!!
            fileType = info.fileType!!
            iconContainer = IconContainer(ImageIcon(info.iconPath), info.iconPath)
            disabled = info.disabled
        }
    }
}