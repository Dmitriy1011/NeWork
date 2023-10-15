package ru.testapp.nework.utils

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ru.testapp.nework.dto.AttachmentType
import ru.testapp.nework.entity.EventUsersPreviewEmbeddable
import ru.testapp.nework.entity.UserPreviewEmbeddable

private val gsonBuilder = GsonBuilder()
private val gson = gsonBuilder.create()
private val listType = TypeToken.getParameterized(List::class.java, String::class.java).type
private val mapType = TypeToken.getParameterized(
    Map::class.java,
    Long::class.java,
    UserPreviewEmbeddable::class.java
).type

class TypeConverter {
    @TypeConverter
    fun fromListToString(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun fromStringToList(string: String): List<String> = gson.fromJson(string, listType)



    @TypeConverter
    fun fromMapToString(map: Map<Long, UserPreviewEmbeddable>): String = gson.toJson(map)

    @TypeConverter
    fun fromStringToMap(string: String): Map<Long, UserPreviewEmbeddable> =
        gson.fromJson(string, mapType)



    @TypeConverter
    fun fromMapToStringEvent(map: Map<Long, EventUsersPreviewEmbeddable>): String = gson.toJson(map)

    @TypeConverter
    fun fromStringToMapEvent(string: String): Map<Long, EventUsersPreviewEmbeddable> =
        gson.fromJson(string, mapType)



    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name

    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)



    @TypeConverter
    fun fromListIntToString(list: List<Int>): String = gson.toJson(list)

    @TypeConverter
    fun toListIntFromString(value: String): List<Int> = gson.fromJson(value, listType)
}