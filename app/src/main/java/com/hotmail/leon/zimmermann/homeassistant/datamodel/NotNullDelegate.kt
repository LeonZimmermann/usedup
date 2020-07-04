package com.hotmail.leon.zimmermann.homeassistant.datamodel

import kotlin.reflect.KProperty

class NotNullDelegate<T>(private var value: T?) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {}
}

class MutableNotNullDelegate<T>(private var value: T?, private val setter: (T) -> Unit) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setter(value)
    }
}