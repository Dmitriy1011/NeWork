package ru.testapp.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityPostRemoteKey(
    @PrimaryKey
    val type: KeyType,
    val key: Long
) {
    enum class KeyType {
        AFTER,
        BEFORE
    }
}