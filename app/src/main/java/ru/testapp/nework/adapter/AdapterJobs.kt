package ru.testapp.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.testapp.nework.databinding.CardJobBinding
import ru.testapp.nework.dto.Job

interface OnIteractionListenerJobs {
    fun onRemove(job: Job) {}
}

class AdapterJobs(
    private val listener: OnIteractionListenerJobs
) : ListAdapter<Job, AdapterJobs.ViewHolderJob>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderJob {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderJob(
            CardJobBinding.inflate(inflater, parent, false),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolderJob, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolderJob(
        private val binding: CardJobBinding,
        private val listener: OnIteractionListenerJobs
    ) : ViewHolder(binding.root) {
        fun bind(job: Job) {
            binding.apply {
                jobName.text = job.name
                startDate.text = job.start
                endDate.text = job.finish
                jobPosition.text = job.position
                jobLink.text = job.link

                deleteJobButton.setOnClickListener {
                    listener.onRemove(job)
                }
            }
        }
    }

    class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
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