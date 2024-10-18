package dev.priporov.customicons.pattern.item

import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType
import java.util.*
import javax.swing.ImageIcon

open class BaseConditionItem(
    var condition: String,
    var conditionType: ConditionType
) {
    val id: String = UUID.randomUUID().toString()

    var fileType: FileType = FileType.FILE
    var icon: ImageIcon? = null
    var disabled: Boolean = false

    override fun toString(): String {
        return condition
    }
}