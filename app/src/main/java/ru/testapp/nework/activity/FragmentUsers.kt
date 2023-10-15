package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterUsers
import ru.testapp.nework.databinding.FragmentUsersBinding
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentUsers : Fragment() {

    private val viewModel: ViewModelUsers by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentUsersBinding.bind(view)

        val adapter = AdapterUsers()

        binding.usersList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.observe(viewLifecycleOwner) {
                    viewModel.loadUsers()
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.usersSwiperRefresh.isVisible = it.refreshing
            binding.usersProgressBar.isVisible = it.refreshing || it.loading
            binding.errorGroup.isVisible = it.error
            if (it.error) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.users_are_not_loaded_try_again),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    getString(R.string.try_loading_users)
                ) {
                    viewModel.loadUsers()
                }.show()
            }
        }

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.navButtonPosts -> findNavController().navigate(R.id.action_fragmentUsers_to_fragmentPostsFeed)
                    R.id.navButtonEvents -> findNavController().navigate(R.id.action_fragmentUsers_to_fragmentEvents)
                }
            }
    }
}