package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterEvents
import ru.testapp.nework.adapter.OnIteractionListenerEvents
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentEventsBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.viewmodel.ViewModelEvents
import javax.inject.Inject

@AndroidEntryPoint
class FragmentEvents : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: ViewModelEvents by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentEventsBinding.bind(view)

        val adapter = AdapterEvents(object : OnIteractionListenerEvents {
            override fun onEdit(event: Event) {
                viewModel.editEvent(event)
            }

            override fun onRemove(event: Event) {
                viewModel.removeEvent(event.id)
            }

            override fun onLike(event: Event) {
                viewModel.likeEvent(event.id)
            }

            override fun onUnLike(event: Event) {
                viewModel.unLikeEvent(event.id)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appAuth.authStateFlow.collect() {
                    adapter.refresh()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swipeRefresh.isRefreshing = state.refresh is LoadState.Loading
                }
            }
        }


        viewModel.eventLoadError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.eventLoadError.value, Toast.LENGTH_LONG)
                .show()
        }

        viewModel.eventSaveError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.eventSaveError.value, Toast.LENGTH_LONG)
        }

        viewModel.eventsState.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isVisible = it.refreshing
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
                    viewModel.loadEvents()
                }.show()
            }
        }

        binding.fab.setOnClickListener {
            if (appAuth.authStateFlow.value != null) {
                findNavController().navigate(R.id.action_fragmentPostsFeed_to_fragmentCreateAndEditPost2)
            }
            Snackbar.make(
                binding.root,
                getString(R.string.fab_click_message),
                Snackbar.LENGTH_LONG
            ).setAction(
                getString(R.string.sign_in),
            ) {
                findNavController().navigate(R.id.action_fragmentPostsFeed_to_fragmentSignIn)
            }.setAction(
                getString(R.string.sign_up)
            ) {
                findNavController().navigate(R.id.action_fragmentPostsFeed_to_fragmentSignUp)
            }.show()
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadEvents()
        }

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.navButtonPosts -> findNavController().navigate(R.id.action_fragmentEvents_to_fragmentPostsFeed)
                    R.id.navButtonUsers -> findNavController().navigate(R.id.action_fragmentEvents_to_fragmentUsers)
                }
            }
    }
}