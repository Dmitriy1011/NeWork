package ru.testapp.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.testapp.nework.dto.User

@Entity
data class EntityUser(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String?
) {
    fun toDto() = User(id, login, name, avatar)

    companion object {
        fun fromDto(dto: User) = EntityUser(dto.id, dto.login, dto.name, dto.avatar)
    }
}

fun List<EntityUser>.toDto(): List<User> = map(EntityUser::toDto)
fun List<User>.fromDto(): List<EntityUser> = map(EntityUser::fromDto)