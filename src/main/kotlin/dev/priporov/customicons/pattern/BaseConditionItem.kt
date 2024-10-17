package dev.priporov.customicons.pattern

import java.util.*

open class BaseConditionItem(
    val condition: String,
    val id: String = UUID.randomUUID().toString()
) {
    override fun toString(): String {
        return condition
    }
}