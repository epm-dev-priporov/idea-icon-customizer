package dev.priporov.customicons.state

import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType

class ConditionItemInfo (
    var condition: String? = null,
    var conditionType: ConditionType? = null,
    var id: String? = null,
    var fileType: FileType? = null,
    var iconPath: String? = null,
    var disabled: Boolean = false
){
    constructor() : this(null, null, null, null, null, false)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConditionItemInfo

        if (condition != other.condition) return false
        if (conditionType != other.conditionType) return false
        if (id != other.id) return false
        if (fileType != other.fileType) return false
        if (iconPath != other.iconPath) return false
        if (disabled != other.disabled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = condition?.hashCode() ?: 0
        result = 31 * result + (conditionType?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (fileType?.hashCode() ?: 0)
        result = 31 * result + (iconPath?.hashCode() ?: 0)
        result = 31 * result + disabled.hashCode()
        return result
    }
}