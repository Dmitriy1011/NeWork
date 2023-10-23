package ru.testapp.nework.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object EventIdArg : ReadWriteProperty<Bundle, Long> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): Long =
        thisRef.getLong(property.name)


    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long) {
        thisRef.putLong(property.name, value)
    }
}
