package ru.testapp.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityRemoteKeyWallUser(
    @PrimaryKey
    val type: KeyType,
    val key: Long
) {
    enum class KeyType {
        AFTER, BEFORE
    }
}
