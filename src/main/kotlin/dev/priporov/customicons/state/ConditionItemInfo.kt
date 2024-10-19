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
)