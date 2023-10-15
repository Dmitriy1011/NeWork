package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.databinding.CardImageBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.handler.loadAvatarImage

class AdapterLikersShortList :
    ListAdapter<Post, AdapterLikersShortList.LikersShortViewHolder>(LikersShortDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikersShortViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LikersShortViewHolder(
            CardImageBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LikersShortViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LikersShortViewHolder(
        private val binding: CardImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                post.users.forEach { entry ->
                    val url = entry.value.avatar.orEmpty()
                    likersPreviewImage.loadAvatarImage(url)
                }

            }
        }
    }

    class LikersShortDiffCallback : DiffUtil.ItemCallback<Post>() {
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