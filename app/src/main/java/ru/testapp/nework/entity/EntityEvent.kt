package ru.testapp.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.EventAttachment
import ru.testapp.nework.dto.EventCoordinates
import ru.testapp.nework.dto.EventUserPreview
import ru.testapp.nework.enum.AttachmentTypeEvent

@Entity
data class EntityEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    @Embedded
    val coordinates: EventCoordinatesEmbeddable?,
    val eventType: String,
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean,
    val speakerIds: List<Int>?,
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean?,
    @Embedded
    val attachment: EventAttachmentEmbeddable?,
    val link: String?,
    val ownedByMe: Boolean = false,
    val users: Map<Long, EventUsersPreviewEmbeddable>,
) {
    fun toDto() = Event(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        datetime,
        published,
        coordinates,
        eventType,
        likeOwnerIds,
        likedByMe,
        speakerIds,
        participantsIds,
        participatedByMe,
        attachment,
        link,
        ownedByMe,
        users
    )

    companion object {
        fun fromDto(dto: Event) = EntityEvent(
            dto.id,
            dto.authorId,
            dto.author,
            dto.authorAvatar,
            dto.authorJob,
            dto.content,
            dto.datetime,
            dto.published,
            dto.coordinates,
            dto.type,
            dto.likeOwnerIds,
            dto.likedByMe,
            dto.speakerIds,
            dto.participantsIds,
            dto.participatedByMe,
            dto.attachment,
            dto.link,
            dto.ownedByMe,
            dto.users
        )
    }
}

data class EventCoordinatesEmbeddable(
    val latitude: String,
    val longitude: String
) {
    fun toDto() = EventCoordinates(latitude, longitude)

    companion object {
        fun fromDto(dto: EventCoordinates) = EventCoordinatesEmbeddable(dto.latitude, dto.longitude)
    }
}

data class EventUsersPreviewEmbeddable(
    val name: String,
    val avatar: String?
) {
    fun toDto() = EventUserPreview(name, avatar)

    companion object {
        fun fromDto(dto: EventUserPreview) = EventUsersPreviewEmbeddable(dto.name, dto.avatar)
    }
}

data class EventAttachmentEmbeddable(
    var url: String,
    val type: String
) {
    fun toDto() = EventAttachment(url, type)

    companion object {
        fun fromDto(dto: EventAttachment) = EventAttachmentEmbeddable(dto.url, dto.type)
    }
}

fun List<EntityEvent>.listEntityEventToEvent(): List<Event> = map(EntityEvent::toDto)
fun List<Event>.listEventToEntityEvent(): List<EntityEvent> = map(EntityEvent::fromDto)
