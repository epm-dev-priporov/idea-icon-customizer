package dev.priporov.customicons.pattern

class RegexpPatternItem(condition: String) : BaseConditionItem(condition) {
    val type: PatternType = PatternType.REGEXP
}