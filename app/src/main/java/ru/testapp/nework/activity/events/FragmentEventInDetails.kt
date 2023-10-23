package ru.testapp.nework.activity.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.events.FragmentEvents.Companion.eventIdArg
import ru.testapp.nework.adapter.AdapterUsersFilteredEvent
import ru.testapp.nework.adapter.OnIteractionListenerUsersFiltered
import ru.testapp.nework.databinding.FragmentEventInDetailsBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.viewmodel.ViewModelEvents
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentEventInDetails : Fragment() {
    private val usersViewModel: ViewModelUsers by viewModels()
    private val eventsViewModel: ViewModelEvents by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventInDetailsBinding.inflate(inflater, container, false)

        eventsViewModel.eventDetailsData.observe(viewLifecycleOwner) { modelEvent ->
            modelEvent.eventsList.find { it.id == arguments?.eventIdArg }?.let { event ->
                binding.eventCardInDetails.apply {
                    speakersTitle.visibility = View.VISIBLE
                    speakersList.visibility = View.VISIBLE
                    likersTitle.visibility = View.VISIBLE
                    likersListShort.visibility = View.VISIBLE
                    moreLikersButton.visibility = View.VISIBLE
                    participantsTitle.visibility = View.VISIBLE
                    eventInDetailsMentionedButton.visibility = View.VISIBLE
                    participantsList.visibility = View.GONE

                    requireActivity().addMenuProvider(object : MenuProvider {
                        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                            menuInflater.inflate(R.menu.menu_event_in_details, menu)
                        }

                        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
                    }, viewLifecycleOwner)


                    val usersFilteredAdapter =
                        AdapterUsersFilteredEvent(object : OnIteractionListenerUsersFiltered {
                            override fun returnEvent(event: Event) {
                                moreLikersButton.setOnClickListener {
                                    findNavController().navigate(
                                        R.id.action_fragmentEventInDetails2_to_fragmentLikersEvent2,
                                        Bundle().apply {
                                            putSerializable("eventKey", event)
                                        }
                                    )
                                }
                            }
                        })

                    likersListShort.adapter = usersFilteredAdapter
                    speakersList.adapter = usersFilteredAdapter
                    participantsList.adapter = usersFilteredAdapter


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
                            val filteredUsers =
                                it.users.filter { it.id == participantOwnerId.toLong() }
                            usersFilteredAdapter.submitList(filteredUsers)
                        }
                    }
                }
            }
        }

        return binding.root
    }
}