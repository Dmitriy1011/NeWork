package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.databinding.CardImageBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.User
import ru.testapp.nework.handler.loadAvatarImage

interface OnIteractionListenerUsersFiltered {
    fun returnEvent(event: Event) {}
}

class AdapterUsersFilteredEvent(
    private val listener: OnIteractionListenerUsersFiltered
) :
    ListAdapter<User, AdapterUsersFilteredEvent.EventUsersFilteredViewHolder>(
        EventUsersFilteredDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventUsersFilteredViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventUsersFilteredViewHolder(
            CardImageBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: EventUsersFilteredViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventUsersFilteredViewHolder(
        private val binding: CardImageBinding,
        private val listener: OnIteractionListenerUsersFiltered
    ) : ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                val ava = user.avatar ?: return
                eventUsersFilteredPreviewImage.loadAvatarImage(ava)
            }
        }
    }

    class EventUsersFilteredDiffCallback : DiffUtil.ItemCallback<User>() {
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