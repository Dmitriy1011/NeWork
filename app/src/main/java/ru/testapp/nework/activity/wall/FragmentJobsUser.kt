package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.adapter.AdapterJobs
import ru.testapp.nework.adapter.OnIteractionListenerJobs
import ru.testapp.nework.databinding.FragmentJobsUserBinding
import ru.testapp.nework.dto.Job
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentJobsUser : Fragment() {

    private val viewModel: ViewModelJobs by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobsUserBinding.inflate(inflater, container, false)

        val adapter = AdapterJobs(object : OnIteractionListenerJobs {
            override fun onRemove(job: Job) {
                viewModel.removeJob(job.id)
            }
        })

        binding.profileUserJobsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.jobDataUser.observe(viewLifecycleOwner) {
                    adapter.submitList(it.jobUser)
                }
            }
        }

        return binding.root
    }
}