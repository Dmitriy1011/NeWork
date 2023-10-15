package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.databinding.CardPostBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.handler.loadAttachmentImage
import ru.testapp.nework.handler.loadAvatarImage

interface OnIteractionListenerWallUser {
    fun onLike(post: Post) {}
    fun onUnLike(post: Post) {}
}

class AdapterWallUser(
    private val listener: OnIteractionListenerWallUser
) : PagingDataAdapter<Post, AdapterWallUser.ViewHolderWallUser>(WallUserDiffCalback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderWallUser {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderWallUser(
            CardPostBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolderWallUser, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ViewHolderWallUser(
        private val binding: CardPostBinding,
        private val listener: OnIteractionListenerWallUser
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                postAuthorName.text = post.author
                postTextContent.text = post.content
                postPublishedDate.text = post.published

                postLikeButton.text = post.likeOwnerIds?.size.toString()

                postLikeButton.setOnClickListener {
                    if (!post.likedByMe) listener.onLike(post) else listener.onUnLike(
                        post
                    )
                }

                postAttachmentImage.isVisible = !post.attachment?.url.isNullOrBlank()

                postMenuButton.isVisible = post.ownedByMe

                val baseUrl = "https://netomedia.ru/"

                val avatarUrl = "${baseUrl}avatars/${post.authorAvatar}"
                val attachmentImageUrl = "${baseUrl}media/${post.attachment?.url}"

                postAvatar.loadAvatarImage(avatarUrl)
                postAttachmentImage.loadAttachmentImage(attachmentImageUrl)
            }
        }
    }

    class WallUserDiffCalback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}