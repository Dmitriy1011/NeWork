package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.databinding.CardUserBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.handler.loadAvatarImage

class AdapterLikersFullList :
    ListAdapter<Post, AdapterLikersFullList.LikersFullViewHolder>(LikersFullDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikersFullViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LikersFullViewHolder(
            CardUserBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LikersFullViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LikersFullViewHolder(
        private val binding: CardUserBinding
    ) : ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                post.users.forEach { entry ->
                    val url = entry.value.avatar.orEmpty()
                    val name = entry.value.name
                    userAvatar.loadAvatarImage(url)
                    userName.text = name
                }
            }
        }
    }

    class LikersFullDiffCallback : DiffUtil.ItemCallback<Post>() {
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