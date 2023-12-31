package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.databinding.CardUserBinding
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage

interface OnIteractionListenerUsers {
    fun onDetailsClick(user: User) {}
}

class AdapterUsers(
    private val listener: OnIteractionListenerUsers
) : ListAdapter<User, AdapterUsers.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(
            CardUserBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(
        private val binding: CardUserBinding,
        private val listener: OnIteractionListenerUsers
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                userName.text = user.name
                userLogin.text = user.login

                val ava = user.avatar ?: return
                userAvatar.loadAvatarImage(ava)

                binding.root.setOnClickListener {
                    listener.onDetailsClick(user)
                }
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
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