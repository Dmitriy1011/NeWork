package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.testapp.nework.adapter.AdapterEventLikers
import ru.testapp.nework.adapter.AdapterEventSpeakers
import ru.testapp.nework.adapter.AdapterEventsParticipants
import ru.testapp.nework.databinding.FragmentEventInDetailsBinding
import ru.testapp.nework.viewmodel.ViewModelEvents

class FragmentEventInDetails : Fragment() {

    private val viewModel: ViewModelEvents by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentEventInDetailsBinding.bind(view)

        val speakersAdapter = AdapterEventSpeakers()
        val likersAdapter = AdapterEventLikers()
        val participantsAdapter = AdapterEventsParticipants()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect()
            }
        }
    }
}