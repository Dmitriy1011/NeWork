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
import ru.testapp.nework.adapter.AdapterPostsLoadingState
import ru.testapp.nework.adapter.OnIteractionListener
import ru.testapp.nework.adapter.PostsAdapter
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentPostsFeedBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelPost
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPostsFeed(

) : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: ViewModelPost by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentPostsFeedBinding.bind(view)

        val adapter = PostsAdapter(object : OnIteractionListener {
            override fun onEdit(post: Post) {
                viewModel.editPost(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removePost(post.id)
            }

            override fun onLike(post: Post) {
                viewModel.likePost(post.id)
            }

            override fun onUnLike(post: Post) {
                viewModel.unLikePost(post.id)
            }
        })

        binding.postsList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = AdapterPostsLoadingState {
                adapter.retry()
            },
            footer = AdapterPostsLoadingState {
                adapter.retry()
            }
        )

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

        viewModel.postLoadError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.postLoadError.value, Toast.LENGTH_LONG)
                .show()
        }

        viewModel.postSaveError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.postSaveError.value, Toast.LENGTH_LONG)
        }

        viewModel.feedState.observe(viewLifecycleOwner) {
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
                    viewModel.loadPosts()
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
            viewModel.loadPosts()
        }

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.navButtonEvents -> findNavController().navigate(R.id.action_fragmentPostsFeed_to_fragmentEvents)
                    R.id.navButtonUsers -> findNavController().navigate(R.id.action_fragmentPostsFeed_to_fragmentUsers)
                }
            }
    }
}