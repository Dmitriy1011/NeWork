package ru.testapp.nework.utils

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ru.testapp.nework.entity.EventUsersPreviewEmbeddable
import ru.testapp.nework.entity.UserPreviewEmbeddable
import java.util.regex.Pattern

private val gsonBuilder = GsonBuilder()
private val gson = gsonBuilder.create()
private val listIntType = TypeToken.getParameterized(List::class.java, Int::class.java).type

class TypeConverter {
    @TypeConverter
    fun fromMapType(currency: String): Map<Long, EventUsersPreviewEmbeddable>? {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            Long::class.java,
            EventUsersPreviewEmbeddable::class.java
        )
        return Moshi.Builder().build().adapter<Map<Long, EventUsersPreviewEmbeddable>>(type)
            .fromJson(currency)
    }

    @TypeConverter
    fun fromString(map: Map<Long, EventUsersPreviewEmbeddable>): String {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            Long::class.java,
            EventUsersPreviewEmbeddable::class.java
        )
        return Moshi.Builder().build().adapter<Map<Long, EventUsersPreviewEmbeddable>>(type)
            .toJson(map)
    }

    @TypeConverter
    fun fromMapTypePost(currency: String): Map<Long, UserPreviewEmbeddable>? {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            Long::class.java,
            UserPreviewEmbeddable::class.java
        )
        return Moshi.Builder().build().adapter<Map<Long, UserPreviewEmbeddable>>(type)
            .fromJson(currency)
    }

    @TypeConverter
    fun fromStringPost(map: Map<Long, UserPreviewEmbeddable>): String {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            Long::class.java,
            UserPreviewEmbeddable::class.java
        )
        return Moshi.Builder().build().adapter<Map<Long, UserPreviewEmbeddable>>(type).toJson(map)
    }


    @TypeConverter
    fun fromListIntToString(list: List<Int>) = list.joinToString(",")

    @TypeConverter
    fun fromStringToListInt(string: String): List<Int> = string.split(",").map { it.toInt() }
}