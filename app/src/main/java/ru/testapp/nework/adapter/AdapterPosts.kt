package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardPostBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.handler.loadAttachmentImage
import ru.testapp.nework.handler.loadAvatarImage


interface OnIteractionListener {
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onLike(post: Post) {}
    fun onUnLike(post: Post) {}
}

class PostsAdapter(
    private val onIteractionListener: OnIteractionListener
) : PagingDataAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCalback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostViewHolder(
            CardPostBinding.inflate(layoutInflater, parent, false),
            onIteractionListener = onIteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class PostViewHolder(
        private val binding: CardPostBinding,
        private val onIteractionListener: OnIteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                postAuthorName.text = post.author
                postTextContent.text = post.content
                postPublishedDate.text = post.published

                postLikeButton.text = post.likeOwnerIds?.size.toString()

                postLikeButton.setOnClickListener {
                    if (!post.likedByMe) onIteractionListener.onLike(post) else onIteractionListener.onUnLike(
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


                postMenuButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.edit -> {
                                    onIteractionListener.onEdit(post)
                                    true
                                }

                                R.id.delete -> {
                                    onIteractionListener.onRemove(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
    }

    class PostDiffCalback : DiffUtil.ItemCallback<Post>() {
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