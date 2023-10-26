package ru.testapp.nework.activity.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterChooseUsers
import ru.testapp.nework.databinding.FragmentChooseUsersBinding
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentChooseUsers : Fragment() {

    private val viewModel: ViewModelPost by viewModels()
    private val viewModelUsers: ViewModelUsers by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChooseUsersBinding.inflate(inflater, container, false)

        var mentionedIds = mutableListOf<Int>()

        val adapter = AdapterChooseUsers()

        binding.mentionedUsersList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelUsers.data.observe(viewLifecycleOwner) {
                    adapter.submitList(it.users)
                }
            }
        }

        adapter.mentionedList.observe(viewLifecycleOwner) { idOfCheckedItem ->
            mentionedIds.add(idOfCheckedItem)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_choose_users, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.saveChooseUser -> {
                        viewModel.saveMentionedIds(mentionedIds)
                        viewModel.savePost()
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        return binding.root
    }
}