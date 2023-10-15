package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardEventBinding
import ru.testapp.nework.dto.Event

interface OnIteractionListenerEvents {
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
    fun onLike(event: Event) {}
    fun onUnLike(event: Event) {}
}

class AdapterEvents(
    private val listener: OnIteractionListenerEvents
) : PagingDataAdapter<Event, AdapterEvents.EventsViewHolder>(EventsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventsViewHolder(
            CardEventBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class EventsViewHolder(
        private val binding: CardEventBinding,
        private val listener: OnIteractionListenerEvents
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                eventAuthorName.text = event.author
                eventPublishedDate.text = event.published
                eventType.text = event.type
                eventDateTime.text = event.datetime
                eventTextContent.text = event.content

                eventLikeButton.text = event.likeOwnerIds?.size.toString()
                eventParticipantsButton.text = event.likeOwnerIds?.size.toString()

                eventMenuButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_event)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.editEvent -> {
                                    listener.onEdit(event)
                                    true
                                }

                                R.id.deleteEvent -> {
                                    listener.onRemove(event)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
    }

    class EventsDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(
            oldItem: Event,
            newItem: Event
        ): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Event,
            newItem: Event
        ): Boolean {
            return oldItem == newItem
        }

    }
}