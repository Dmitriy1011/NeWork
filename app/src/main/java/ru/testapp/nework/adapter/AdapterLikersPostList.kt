package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.databinding.CardUserBinding
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage

class AdapterLikersPostList : ListAdapter<User, AdapterLikersPostList.LikersPostViewHolder>(LikersPostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikersPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LikersPostViewHolder(
            CardUserBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LikersPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LikersPostViewHolder(
        private val binding: CardUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {

                userName.text = user.name
                userLogin.text = user.login

                userAvatar.loadAvatarImage(user.avatar)
            }
        }
    }

    class LikersPostDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}