package ru.testapp.nework.activity.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterChooseUsers
import ru.testapp.nework.adapter.OnIteractionListenerChooseUsers
import ru.testapp.nework.databinding.FragmentChooseUsersBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.dto.User
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelUsers
import javax.inject.Inject

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

        val adapter = AdapterChooseUsers(object : OnIteractionListenerChooseUsers {
            override fun returnPostForTransfer(post: Post) {
                return
            }
        })

        val post = requireArguments().getSerializable("postKey") as Post

        adapter.mentionedList.observe(viewLifecycleOwner) { idOfCheckedItem ->
            mentionedIds.add(idOfCheckedItem)
        }

        binding.mentionedUsersList.adapter = adapter

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

        viewModelUsers.data.observe(viewLifecycleOwner) {
            val mentionIds = post.mentionIds.orEmpty().toSet()
            mentionIds.forEach { listItem ->
                val filteredUsers = it.users.filter { it.id == listItem.toLong() }
                adapter.submitList(filteredUsers)
            }
        }

        return binding.root
    }
}