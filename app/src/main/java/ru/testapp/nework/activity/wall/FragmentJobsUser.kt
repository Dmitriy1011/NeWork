package ru.testapp.nework.activity.wall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

import ru.testapp.nework.adapter.AdapterJobsUser
import ru.testapp.nework.adapter.OnIteractionListenerJobsUser
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

        val adapter = AdapterJobsUser(object : OnIteractionListenerJobsUser {
            override fun followingTheLink(job: Job) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link))
                startActivity(intent)
            }
        })

        binding.profileUserJobsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.jobDataUser.observe(viewLifecycleOwner) { modelJobUser ->
                    if (modelJobUser.jobUserList.isEmpty()) {
                        binding.userJobsEmptyText.visibility = View.VISIBLE
                    }
                    adapter.submitList(modelJobUser.jobUserList)
                }
            }
        }


        viewModel.loadingJobUserState.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it.refreshing
            binding.progressBar.isVisible = it.loading || it.refreshing
            binding.errorGroup.isVisible = it.error
            if (it.error) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.something_went_wrong_try_again),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    getString(R.string.retry)
                ) {
                    viewModel.userIdState.observe(viewLifecycleOwner) { userId ->
                        viewModel.loadJobsUser(userId.toLong())
                    }
                }.show()
            }
        }

        return binding.root
    }
}