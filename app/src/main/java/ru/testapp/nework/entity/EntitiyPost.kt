package ru.testapp.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.testapp.nework.dto.Attachment
import ru.testapp.nework.dto.AttachmentType
import ru.testapp.nework.dto.Coordinates
import ru.testapp.nework.dto.Post
import ru.testapp.nework.dto.UserPreview

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    @Embedded
    val coordinates: CoordinatesEmbeddable? = null,
    val link: String?,
    val likeOwnerIds: List<Int>?,
    val mentionIds: List<Int>?,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val ownedByMe: Boolean,
    @Embedded
    val users: Map<Long, UserPreviewEmbeddable>
) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        published,
        coordinates,
        link,
        likeOwnerIds,
        mentionIds,
        mentionedMe,
        likedByMe,
        attachment,
        ownedByMe,
        users
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.authorId,
            dto.author,
            dto.authorAvatar,
            dto.authorJob,
            dto.content,
            dto.published,
            dto.coordinates,
            dto.link,
            dto.likeOwnerIds,
            dto.mentionIds,
            dto.mentionedMe,
            dto.likedByMe,
            dto.attachment,
            dto.ownedByMe,
            dto.users
        )
    }
}


data class CoordinatesEmbeddable(
    val latitude: String,
    val longitude: String
) {
    fun toDto() = Coordinates(latitude, longitude)

    companion object {
        fun fromDto(dto: Coordinates) = CoordinatesEmbeddable(dto.latitude, dto.longitude)
    }
}


data class AttachmentEmbeddable(
    val url: String,
    val type: AttachmentType
) {

    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment) = AttachmentEmbeddable(dto.url, dto.type)
    }
}

data class UserPreviewEmbeddable(
    val name: String,
    val avatar: String?
) {
    fun toDto() = UserPreview(name, avatar)

    companion object {
        fun fromDto(dto: UserPreview) = UserPreviewEmbeddable(dto.name, dto.avatar)
    }
}

fun List<PostEntity>.listToDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.listFromDto(): List<PostEntity> = map(PostEntity::fromDto)