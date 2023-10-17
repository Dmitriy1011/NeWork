package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.testapp.nework.R
import ru.testapp.nework.activity.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.databinding.FragmentImageSeparateBinding
import ru.testapp.nework.handler.loadAttachmentImage

class FragmentAttachmentSeparate : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentImageSeparateBinding.bind(view)

        arguments?.textArg?.let(binding.separateImage::loadAttachmentImage)

        binding.fromSeparateToFeedButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAttachmentSeparate_to_fragmentPostsFeed)
        }
    }
}