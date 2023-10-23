package ru.testapp.nework.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.testapp.nework.entity.UserPreviewEmbeddable

private val gson = Gson()
private val usersType = object : TypeToken<Map<Long, UserPreviewEmbeddable>>(){}.type
private val listType = object : TypeToken<List<Int>>(){}.type

class TypeConverter {
    @TypeConverter
    fun fromListIntToString(list: List<Int>): String = gson.toJson(list)

    @TypeConverter
    fun fromStringToListInt(value: String): List<Int> = gson.fromJson(value, listType)

    @TypeConverter fun fromMap(map: Map<Long, UserPreviewEmbeddable>): String {
        return gson.toJson(map)
    }

    @TypeConverter fun toMap(usersSerialiazed: String): Map<Long, UserPreviewEmbeddable> = gson.fromJson(usersSerialiazed, usersType)
}