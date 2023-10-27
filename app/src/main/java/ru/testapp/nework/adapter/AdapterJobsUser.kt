package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.testapp.nework.databinding.CardJobUserBinding
import ru.testapp.nework.dto.Job

interface OnIteractionListenerJobsUser {
    fun followingTheLink(job: Job) {}
}

class AdapterJobsUser(
    private val listener: OnIteractionListenerJobsUser
) :
    ListAdapter<Job, AdapterJobsUser.CardJobUserViewHolder>(DiffCallbackCardJobUser()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardJobUserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardJobUserViewHolder(
            CardJobUserBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: CardJobUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CardJobUserViewHolder(
        private val binding: CardJobUserBinding,
        private val listener: OnIteractionListenerJobsUser
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Job) {
            binding.apply {
                jobName.text = job.name
                startDate.text = job.start
                endDate.text = job.finish
                jobPosition.text = job.position

                jobLink.isVisible = job.link.isNullOrBlank()
                jobLink.text = job.link

                binding.jobLink.setOnClickListener {
                    listener.followingTheLink(job)
                }
            }
        }
    }

    class DiffCallbackCardJobUser : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}