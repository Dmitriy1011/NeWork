package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.databinding.CardUserBinding
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage

class AdapterLikersEventFull :
    ListAdapter<User, AdapterLikersEventFull.LikersFullViewHolder>(LikersFullDiffCallback()) {

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
        fun bind(user: User) {
            binding.apply {

                userName.text = user.name
                userLogin.text = user.login

                val ava = user.avatar ?: return
                userAvatar.loadAvatarImage(ava)
            }
        }
    }

    class LikersFullDiffCallback : DiffUtil.ItemCallback<User>() {
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