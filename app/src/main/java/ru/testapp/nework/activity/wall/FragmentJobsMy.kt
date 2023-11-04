package ru.testapp.nework.activity.wall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterJobs
import ru.testapp.nework.adapter.OnIteractionListenerJobs
import ru.testapp.nework.databinding.FragmentJobsMyBinding
import ru.testapp.nework.dto.Job
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentJobsMy : Fragment() {

    private val viewModel: ViewModelJobs by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobsMyBinding.inflate(inflater, container, false)

        val adapter = AdapterJobs(object : OnIteractionListenerJobs {

            override fun followingTheLink(job: Job) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link))
                startActivity(intent)
            }

            override fun onRemove(job: Job) {
                viewModel.removeJob(job.id)
            }
        })

        binding.profileMyJobsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.jobDataMy.observe(viewLifecycleOwner) { modelJobMy ->
                    if (modelJobMy.jobsMy.isEmpty()) {
                        binding.myJobsEmptyText.visibility = View.VISIBLE
                    } else {
                        adapter.submitList(modelJobMy.jobsMy)
                    }
                }
            }
        }

        viewModel.loadingJobMyState.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        viewModel.loadingJobMyState.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it.refreshing || it.loading
            binding.progressBar.isVisible = it.loading
            if (it.error) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.something_went_wrong_try_again),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    getString(R.string.retry)
                ) {
                    viewModel.loadJobsMy()
                }.show()
            }
        }

        return binding.root
    }
}