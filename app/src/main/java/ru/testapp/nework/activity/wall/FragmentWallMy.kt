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
import ru.testapp.nework.adapter.AdapterWallMy
import ru.testapp.nework.adapter.OnIteractionListenerWallMy
import ru.testapp.nework.databinding.FragmentWallMyBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelWallMy

@AndroidEntryPoint
class FragmentWallMy : Fragment() {

    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelWallMy: ViewModelWallMy by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWallMyBinding.inflate(inflater, container, false)

        val adapter = AdapterWallMy(object : OnIteractionListenerWallMy {
            override fun onEdit(post: Post) {
                viewModelPost.editPost(post)
            }

            override fun onRemove(post: Post) {
                viewModelPost.removePost(post.id)
            }

            override fun onLike(post: Post) {
                viewModelPost.likePost(post.id)
            }

            override fun onUnLike(post: Post) {
                viewModelPost.unLikePost(post.id)
            }
        })

        binding.wallMyList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelWallMy.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        return binding.root
    }
}