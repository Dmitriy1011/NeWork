package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.adapter.AdapterJobs
import ru.testapp.nework.databinding.FragmentJobsUserBinding
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentJobsUser : Fragment() {

    private val viewModel: ViewModelJobs by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentJobsUserBinding.bind(view)

        val adapter = AdapterJobs()

        binding.profileUserJobsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.jobDataUser.observe(viewLifecycleOwner) {
                    adapter.submitList(it.jobUser)
                }
            }
        }
    }
}