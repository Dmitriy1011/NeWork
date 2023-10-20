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

class AdapterChooseUsers : ListAdapter<User, AdapterChooseUsers.UserChooseViewHolder>(UserChooseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChooseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserChooseViewHolder(
            CardUserBinding.inflate(inflater, parent, false),
        )
    }

    override fun onBindViewHolder(holder: UserChooseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserChooseViewHolder(
        private val binding: CardUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                userName.text = user.name
                userLogin.text = user.login

                val userAvaUrl = "${BuildConfig.BASE_URL}}avatars/${user.avatar}"

                userAvatar.loadAvatarImage(userAvaUrl)
            }
        }
    }

    class UserChooseDiffCallback : DiffUtil.ItemCallback<User>() {
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