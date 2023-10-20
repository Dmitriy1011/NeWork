package ru.testapp.nework.activity.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterUsersFilteredEvent
import ru.testapp.nework.databinding.FragmentEventInDetailsBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.dto.Post
import ru.testapp.nework.viewmodel.ViewModelEvents
import ru.testapp.nework.viewmodel.ViewModelUsers
import javax.inject.Inject

@AndroidEntryPoint
class FragmentEventInDetails @Inject constructor(
    private val event: Event
) : Fragment() {
    private val usersViewModel: ViewModelUsers by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventInDetailsBinding.inflate(inflater, container, false)

        findNavController().navigate(R.id.fragmentEventInDetails2, Bundle().apply {
            putSerializable("eventKey", event)
        })

        val event = requireArguments().getSerializable("eventKey") as Event

        val usersFilteredAdapter = AdapterUsersFilteredEvent()

        binding.likersListShort.adapter = usersFilteredAdapter
        binding.speakersList.adapter = usersFilteredAdapter
        binding.participantsList.adapter = usersFilteredAdapter

        binding.moreLikersButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentEventInDetails2_to_fragmentLikersEvent2)
        }

        usersViewModel.data.observe(viewLifecycleOwner) {
            val likerOwnerIds = event.likeOwnerIds.orEmpty().toSet()
            likerOwnerIds.forEach { likerOwnerId ->
                val filteredUsers = it.users.filter { it.id == likerOwnerId.toLong() }
                usersFilteredAdapter.submitList(filteredUsers)
            }
        }

        usersViewModel.data.observe(viewLifecycleOwner) {
            val speakerOwnerIds = event.speakerIds.orEmpty().toSet()
            speakerOwnerIds.forEach { speakerOwnerId ->
                val filteredUsers = it.users.filter { it.id == speakerOwnerId.toLong() }
                usersFilteredAdapter.submitList(filteredUsers)
            }
        }

        usersViewModel.data.observe(viewLifecycleOwner) {
            val participantOwnerIds = event.participantsIds.orEmpty().toSet()
            participantOwnerIds.forEach { participantOwnerId ->
                val filteredUsers = it.users.filter { it.id == participantOwnerId.toLong() }
                usersFilteredAdapter.submitList(filteredUsers)
            }
        }



        return binding.root
    }
}