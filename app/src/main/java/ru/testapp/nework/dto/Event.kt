package ru.testapp.nework.dto

import com.google.gson.annotations.SerializedName
import ru.testapp.nework.entity.EventAttachmentEmbeddable
import ru.testapp.nework.entity.EventCoordinatesEmbeddable
import ru.testapp.nework.entity.UserPreviewEmbeddable
import java.io.Serializable

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    @SerializedName("coords")
    val coordinates: EventCoordinatesEmbeddable?,
    val type: String,
    val likeOwnerIds: List<Int>?,
    val likedByMe: Boolean,
    val speakerIds: List<Int>?,
    val participantsIds: List<Int>?,
    val participatedByMe: Boolean?,
    val attachment: EventAttachmentEmbeddable?,
    val link: String?,
    val ownedByMe: Boolean = false,
    val users: Map<Long, UserPreviewEmbeddable>,
    val likes: Int
) : Serializable

data class EventCoordinates(
    val latitude: String,
    val longitude: String
)

data class EventAttachment(
    var url: String,
    val type: String
)