package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.testapp.nework.adapter.AdapterLikersShortList
import ru.testapp.nework.databinding.FragmentPostInDetailsBinding

class FragmentPostInDetails : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentPostInDetailsBinding.bind(view)

        val likersAdapter = AdapterLikersShortList()

        binding.likersListShort.adapter = likersAdapter
    }
}