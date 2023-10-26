package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterWallUser
import ru.testapp.nework.adapter.OnIteractionListenerWallUser
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentWallUsersBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelWallMy
import ru.testapp.nework.viewmodel.ViewModelWallUser
import javax.inject.Inject

@AndroidEntryPoint
class FragmentWallUser : Fragment() {

    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelWallUser: ViewModelWallUser by viewModels()

    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWallUsersBinding.inflate(inflater, container, false)

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
                viewModelWallUser.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appAuth.authStateFlow.collect {
                    adapter.refresh()
                }
            }
        }

        return binding.root
    }
}