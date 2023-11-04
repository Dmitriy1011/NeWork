package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardMentionedUserBinding
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage


class AdapterChooseUsers :
    ListAdapter<User, AdapterChooseUsers.UserChooseViewHolder>(UserChooseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChooseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserChooseViewHolder(
            CardMentionedUserBinding.inflate(inflater, parent, false)
        )
    }

    private val _mentionedList = MutableLiveData<Int>()
    val mentionedList: LiveData<Int>
        get() = _mentionedList

    override fun onBindViewHolder(holder: UserChooseViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.findViewById<CheckBox>(R.id.mentionedUserCheckBox)
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    _mentionedList.value = getItemId(position).toInt()
                }
            }
    }

    class UserChooseViewHolder(
        private val binding: CardMentionedUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                mentionedUserName.text = user.name
                mentionedUserLogin.text = user.login

                val userAvatar = user.avatar ?: return
                mentionedUserAvatar.loadAvatarImage(userAvatar)
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