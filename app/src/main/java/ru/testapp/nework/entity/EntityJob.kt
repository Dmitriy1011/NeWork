package ru.testapp.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.testapp.nework.dto.Job

@Entity
data class EntityJob(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?
) {
    fun toDto() = Job(id, name, position, start, finish, link)

    companion object {
        fun fromDto(dto: Job) = EntityJob(
            dto.id,
            dto.name,
            dto.position,
            dto.start,
            dto.finish,
            dto.link
        )
    }
}

fun List<EntityJob>.listToDto(): List<Job> = map(EntityJob::toDto)
fun List<Job>.listFromDto(): List<EntityJob> = map(EntityJob::fromDto)