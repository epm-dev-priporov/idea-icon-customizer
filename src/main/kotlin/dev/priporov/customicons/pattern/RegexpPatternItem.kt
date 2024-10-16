package dev.priporov.customicons.pattern

class RegexpPatternItem(
    val regex: String
) {
    val type: PatternType = PatternType.REGEXP

    override fun toString(): String {
        return regex
    }
}