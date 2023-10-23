package ru.testapp.nework.activity.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.adapter.AdapterEvents
import ru.testapp.nework.adapter.OnIteractionListenerEvents
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentEventsBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.utils.EventIdArg
import ru.testapp.nework.viewmodel.ViewModelEvents
import javax.inject.Inject

@AndroidEntryPoint
class FragmentEvents : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: ViewModelEvents by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_events_feed, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.authorizeMenuEventsFeed -> {
                        findNavController().navigate(R.id.action_fragmentEvents_to_fragmentProfileMy)
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

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

            override fun onOpenImage(event: Event) {
                findNavController().navigate(
                    R.id.action_fragmentPostsFeed_to_fragmentAttachmentSeparate,
                    Bundle().apply {
                        textArg = "${BuildConfig.BASE_URL}media/${event.attachment?.url}"
                    }
                )
            }

            override fun onOpenVideo(event: Event) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                val chooserIntent = Intent.createChooser(
                    intent,
                    getString(R.string.choose_where_open_your_video)
                )
                startActivity(chooserIntent)
            }

            override fun onOpenAudio(event: Event) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                val chooserIntent = Intent.createChooser(
                    intent,
                    getString(R.string.choose_where_open_your_audio)
                )
                startActivity(chooserIntent)
            }

            override fun followingTheLink(event: Event) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(intent)
            }

            override fun onDetailsClicked(event: Event) {
                findNavController().navigate(
                    R.id.action_fragmentEvents_to_fragmentEventInDetails2,
                    Bundle().apply {
                        eventIdArg = event.id
                    }
                )
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
                    viewModel.loadEvents()
                }.show()
            }
        }

        binding.fab.setOnClickListener {
            if (appAuth.authStateFlow.value != null) {
                findNavController().navigate(R.id.action_fragmentEvents_to_fragmentCreateAndEditEvent)
            }
            Snackbar.make(
                binding.root,
                getString(R.string.fab_click_message),
                Snackbar.LENGTH_LONG
            ).setAction(
                getString(R.string.sign_in),
            ) {
                findNavController().navigate(R.id.action_fragmentEvents_to_fragmentSignIn)
            }.setAction(
                getString(R.string.sign_up)
            ) {
                findNavController().navigate(R.id.action_fragmentEvents_to_fragmentSignUp)
            }.show()
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadEvents()
        }

        return binding.root
    }

    companion object {
        var Bundle.eventIdArg: Long by EventIdArg
    }
}