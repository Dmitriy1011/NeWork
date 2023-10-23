package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardEventBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.Job
import ru.testapp.nework.enum.AttachmentTypeEvent
import ru.testapp.nework.enum.TypeStatus
import ru.testapp.nework.handler.loadAttachmentImage
import ru.testapp.nework.handler.loadAvatarImage

interface OnIteractionListenerEvents {
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
    fun onLike(event: Event) {}
    fun onUnLike(event: Event) {}
    fun onOpenImage(event: Event) {}
    fun onOpenVideo(event: Event) {}
    fun onOpenAudio(event: Event) {}
    fun followingTheLink(event: Event) {}
    fun onDetailsClicked(event: Event) {}
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
                eventDateTime.text = event.datetime
                eventTextContent.text = event.content

                eventLink.isVisible = event.link.isNullOrBlank()
                eventLink.text = event.link

                binding.eventLink.setOnClickListener {
                    listener.followingTheLink(event)
                }

                eventLikeButton.text = event.likeOwnerIds?.size.toString()
                eventParticipantsButton.text = event.likeOwnerIds?.size.toString()

                binding.root.setOnClickListener {
                    listener.onDetailsClicked(event)
                }

                //attachment
                val attachmentType = event.attachment?.type
                val statusType = event.type
                val instanceAttachmentUrl = event.attachment?.url

                eventPlayButton.isVisible = false

                when (attachmentType) {
                    AttachmentTypeEvent.VIDEO.toString() -> eventPlayButton.isVisible = true
                }

                when(statusType) {
                    TypeStatus.ONLINE.toString() -> eventType.text = "Online"
                    TypeStatus.OFFLINE.toString() -> eventType.text = "Offline"
                }

                eventAttachmentImage.isVisible = !instanceAttachmentUrl.isNullOrBlank()

                eventAttachmentImage.setOnClickListener {
                    when(attachmentType) {
                        AttachmentTypeEvent.IMAGE.toString() -> listener.onOpenImage(event)
                        AttachmentTypeEvent.VIDEO.toString() -> listener.onOpenVideo(event)
                        AttachmentTypeEvent.AUDIO.toString() -> listener.onOpenAudio(event)
                        else -> {}
                    }
                }

                val avatarUrl = event.authorAvatar ?: return
                val attachmentImageUrl = event.attachment?.url ?: return

                eventAvatar.loadAvatarImage(avatarUrl)
                eventAttachmentImage.loadAttachmentImage(attachmentImageUrl)

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