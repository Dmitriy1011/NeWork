package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.databinding.CardImageBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.handler.loadAvatarImage

class AdapterEventSpeakers :
    ListAdapter<Event, AdapterEventSpeakers.EventSpeakersViewHolder>(EventSpeakersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSpeakersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventSpeakersViewHolder(
            CardImageBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventSpeakersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventSpeakersViewHolder(
        private val binding: CardImageBinding
    ) : ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                event.users.forEach { entry ->
                    val url = entry.value.avatar.orEmpty()
                    likersPreviewImage.loadAvatarImage(url)
                }
            }
        }
    }

    class EventSpeakersDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}