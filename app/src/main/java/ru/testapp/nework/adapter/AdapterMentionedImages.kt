package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.databinding.CardMentionedImageBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.handler.loadAvatarImage

class AdapterMentionedImages :
    ListAdapter<Post, AdapterMentionedImages.ViewHolderMentionedImages>(MentionedImagesDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderMentionedImages {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderMentionedImages(
            CardMentionedImageBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolderMentionedImages,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolderMentionedImages(
        val binding: CardMentionedImageBinding
    ) : ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                val url = post.authorAvatar ?: return
                mentionedPreviewImage.loadAvatarImage(url)
            }
        }
    }
}

class MentionedImagesDiffCallback : DiffUtil.ItemCallback<Post>() {
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
