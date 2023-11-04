package ru.testapp.nework.activity.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.databinding.FragmentImageSeparateEventBinding
import ru.testapp.nework.handler.loadAttachmentImage

class FragmentAttachmentSeparateEvent : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageSeparateEventBinding.inflate(inflater, container, false)

        arguments?.textArg?.let(binding.separateImage::loadAttachmentImage)

        binding.fromSeparateToFeedButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAttachmentSeparateEvent_to_fragmentEventInDetails2)
        }

        return binding.root
    }
}