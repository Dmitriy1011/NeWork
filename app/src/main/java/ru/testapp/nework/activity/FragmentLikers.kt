package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.testapp.nework.adapter.AdapterLikersFullList
import ru.testapp.nework.databinding.FragmentLikersBinding

class FragmentLikers : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentLikersBinding.bind(view)

        val adapter = AdapterLikersFullList()

        binding.likersListFull.adapter = adapter
    }
}