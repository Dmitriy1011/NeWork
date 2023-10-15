package ru.testapp.nework.dto

import com.google.gson.annotations.SerializedName
import ru.testapp.nework.entity.AttachmentEmbeddable
import ru.testapp.nework.entity.CoordinatesEmbeddable
import ru.testapp.nework.entity.UserPreviewEmbeddable

data class Post(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    @SerializedName("coords")
    val coordinates: CoordinatesEmbeddable? = null,
    val link: String?,
    val likeOwnerIds: List<Int>?,
    val mentionIds: List<Int>?,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: AttachmentEmbeddable? = null,
    val ownedByMe: Boolean = false,
    val users: Map<Long, UserPreviewEmbeddable>,
)

data class Coordinates(
    val latitude: String,
    val longitude: String
)

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class UserPreview(
    val name: String,
    val avatar: String?
)