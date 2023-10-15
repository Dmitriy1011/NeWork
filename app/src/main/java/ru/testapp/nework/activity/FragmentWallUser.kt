package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.testapp.nework.adapter.AdapterWallUser
import ru.testapp.nework.adapter.OnIteractionListenerWallUser
import ru.testapp.nework.databinding.FragmentWallUsersBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelWallMy

@AndroidEntryPoint
class FragmentWallUser : Fragment() {

    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelWallMy: ViewModelWallMy by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentWallUsersBinding.bind(view)

        val adapter = AdapterWallUser(object : OnIteractionListenerWallUser {
            override fun onLike(post: Post) {
                viewModelPost.likePost(post.id)
            }

            override fun onUnLike(post: Post) {
                viewModelPost.unLikePost(post.id)
            }
        })

        binding.wallUsersList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelWallMy.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}