package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.testapp.nework.databinding.FragmentJobCreateBinding

class FragmentJobCreate : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentJobCreateBinding.bind(view)

        binding.buttonCreateJob.setOnClickListener {

        }
    }
}