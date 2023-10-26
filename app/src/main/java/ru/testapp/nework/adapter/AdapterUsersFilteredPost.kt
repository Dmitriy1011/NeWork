package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.databinding.CardImageBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage

class AdapterUsersFilteredPost :
    ListAdapter<User, AdapterUsersFilteredPost.ViewHolderUsersFilteredPost>(
        DiffCallBackUsersFilteredPost()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUsersFilteredPost {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderUsersFilteredPost(
            CardImageBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderUsersFilteredPost, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolderUsersFilteredPost(
        private val binding: CardImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                val ava = user.avatar ?: return
                eventUsersFilteredPreviewImage.loadAvatarImage(ava)
            }
        }
    }

    class DiffCallBackUsersFilteredPost : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
}