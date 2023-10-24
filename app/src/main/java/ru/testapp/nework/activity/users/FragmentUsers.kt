package ru.testapp.nework.activity.users

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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterUsers
import ru.testapp.nework.adapter.OnIteractionListenerUsers
import ru.testapp.nework.databinding.FragmentUsersBinding
import ru.testapp.nework.dto.User
import ru.testapp.nework.utils.UserIdArg
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentUsers : Fragment() {

    private val viewModel: ViewModelUsers by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)

        val adapter = AdapterUsers(object : OnIteractionListenerUsers {
            override fun onDetailsClick(user: User) {
                findNavController().navigate(
                    R.id.action_fragmentUsers_to_fragmentProfileUser,
                    Bundle().apply {
                        userIdArg = user.id
                    }
                )
            }
        })

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_users, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.authorizeUsers -> {
                        findNavController().navigate(R.id.action_fragmentUsers_to_fragmentProfileMy)
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        binding.usersList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.observe(viewLifecycleOwner) {
                    adapter.submitList(it.users)
                }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.usersSwiperRefresh.isRefreshing = it.refreshing
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

        viewModel.usersLoadError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.usersLoadError.value, Toast.LENGTH_LONG)
                .show()
        }

        binding.usersRetryButton.setOnClickListener {
            viewModel.loadUsers()
        }

        return binding.root
    }

    companion object {
        var Bundle.userIdArg: Long by UserIdArg
    }
}