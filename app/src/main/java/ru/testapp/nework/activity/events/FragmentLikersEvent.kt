package ru.testapp.nework.activity.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.adapter.AdapterLikersEventFull
import ru.testapp.nework.databinding.FragmentLikersEventBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentLikersEvent : Fragment() {
    private val viewModelUsers: ViewModelUsers by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLikersEventBinding.inflate(inflater, container, false)

        val adapter = AdapterLikersEventFull()

        val event = requireArguments().getSerializable("eventKey") as Event

        binding.likersListFull.adapter = adapter

        viewModelUsers.data.observe(viewLifecycleOwner) {
            val likeOwnerIds = event.likeOwnerIds.orEmpty().toSet()
            if (likeOwnerIds.isNotEmpty()) {
                likeOwnerIds.forEach { likeOwnerId ->
                    val filteredUsers = it.users.filter { it.id == likeOwnerId.toLong() }
                    adapter.submitList(filteredUsers)
                }
            }
            binding.emptyEventLikersText.visibility = View.VISIBLE
        }

        return binding.root
    }
}