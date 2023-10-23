package ru.testapp.nework.activity.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.databinding.FragmentImageSeparateBinding
import ru.testapp.nework.handler.loadAttachmentImage
import ru.testapp.nework.utils.SeparateIdPostArg

@AndroidEntryPoint
class FragmentAttachmentSeparate : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentImageSeparateBinding.inflate(inflater, container, false)

        arguments?.textArg?.let(binding.separateImage::loadAttachmentImage)

        binding.fromSeparateToFeedButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAttachmentSeparate_to_fragmentPostsFeed)
        }

        return binding.root
    }
}