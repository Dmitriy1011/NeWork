package ru.testapp.nework.activity.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterLikersPostList
import ru.testapp.nework.databinding.FragmentLikersPostBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelUsers
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPostLikers : Fragment() {
    private val viewModelUsers: ViewModelUsers by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLikersPostBinding.inflate(inflater, container, false)

        val adapter = AdapterLikersPostList()

        val post = requireArguments().getSerializable("postKey") as Post

        binding.likersPostListFull.adapter = adapter

        viewModelUsers.data.observe(viewLifecycleOwner) {
            val postLikeOwnerIds = post.likeOwnerIds.orEmpty().toSet()
            postLikeOwnerIds.forEach { postLikeOwnerId ->
                val usersFiltered = it.users.filter { it.id == postLikeOwnerId.toLong() }
                adapter.submitList(usersFiltered)
            }
        }

        return binding.root
    }
}