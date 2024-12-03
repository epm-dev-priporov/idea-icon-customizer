package dev.priporov.customicons.state

import com.intellij.util.xmlb.annotations.Transient
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.pattern.item.IconContainer
import javax.swing.ImageIcon


class PluginState {

    var versionIcon:String? = null
    var items = HashMap<String, ConditionItemInfo>()

    constructor(versionIcon: String?, items: HashMap<String, ConditionItemInfo>) {
        this.versionIcon = versionIcon
        this.items = items
    }

    constructor()

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

    @Transient
    fun getItems(): List<BaseConditionItem> {
        return items.values
            .asSequence()
            .map {toItem(it)}
            .toList()
    }

    @Transient
    private fun toItem(info: ConditionItemInfo): BaseConditionItem {
        return BaseConditionItem(info.condition!!, info.conditionType!!).apply {
            id = info.id!!
            fileType = info.fileType!!
            iconContainer = IconContainer(ImageIcon(info.iconPath), info.iconPath)
            disabled = info.disabled
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PluginState

        if (versionIcon != other.versionIcon) return false
        if (items != other.items) return false

        return true
    }

    override fun hashCode(): Int {
        var result = versionIcon?.hashCode() ?: 0
        result = 31 * result + items.hashCode()
        return result
    }

}