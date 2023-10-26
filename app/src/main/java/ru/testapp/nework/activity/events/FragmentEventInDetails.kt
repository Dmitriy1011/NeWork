package ru.testapp.nework.activity.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.R
import ru.testapp.nework.activity.events.FragmentEvents.Companion.eventIdArg
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.adapter.AdapterEvents
import ru.testapp.nework.adapter.AdapterUsersFilteredEvent
import ru.testapp.nework.adapter.OnIteractionListenerEvents
import ru.testapp.nework.adapter.OnIteractionListenerUsersFiltered
import ru.testapp.nework.databinding.FragmentEventInDetailsBinding
import ru.testapp.nework.dto.Event
import ru.testapp.nework.viewmodel.ViewModelEvents
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentEventInDetails : Fragment() {
    private val usersViewModel: ViewModelUsers by viewModels()
    private val eventsViewModel: ViewModelEvents by viewModels()

    private var mapView: MapView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventInDetailsBinding.inflate(inflater, container, false)

        val currentMenuProvider: MenuProvider? = null

        var idArg = arguments?.eventIdArg

        eventsViewModel.eventDetailsData.observe(viewLifecycleOwner) { modelEvent ->
            modelEvent.eventsList.map { it.copy(id = idArg!!) }
            currentMenuProvider?.let { requireActivity().removeMenuProvider(currentMenuProvider) }
            modelEvent.eventsList.find { it.id == idArg }?.let { event ->
                binding.eventCardInDetails.apply {
                    likersTitle.visibility = View.VISIBLE
                    participantsTitle.visibility = View.VISIBLE
                    eventInDetailsMentionedButton.visibility = View.VISIBLE
                    eventParticipantsButton.visibility = View.GONE

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

                    AdapterEvents.EventsViewHolder(this, object : OnIteractionListenerEvents {
                        override fun onEdit(event: Event) {
                            eventsViewModel.editEvent(event)
                        }

                        override fun onRemove(event: Event) {
                            eventsViewModel.removeEvent(event.id)
                        }

                        override fun onLike(event: Event) {
                            eventsViewModel.likeEvent(event.id)
                        }

                        override fun onUnLike(event: Event) {
                            eventsViewModel.unLikeEvent(event.id)
                        }

                        override fun onOpenImage(event: Event) {
                            findNavController().navigate(
                                R.id.action_fragmentPostInDetails_to_fragmentAttachmentSeparate,
                                Bundle().apply {
                                    textArg =
                                        "${BuildConfig.BASE_URL}media/${event.attachment?.url}"
                                }
                            )
                        }

                        override fun onOpenVideo(event: Event) {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                            val chooserIntent = Intent.createChooser(
                                intent,
                                getString(R.string.choose_where_open_your_video)
                            )
                            startActivity(chooserIntent)
                        }

                        override fun onOpenAudio(event: Event) {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(event.attachment?.url))
                            val chooserIntent = Intent.createChooser(
                                intent,
                                getString(R.string.choose_where_open_your_audio)
                            )
                            startActivity(chooserIntent)
                        }

                        override fun followingTheLink(event: Event) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                            startActivity(intent)
                        }
                    }).bind(event)

                    likersListShort.adapter = usersFilteredAdapter
                    speakersList.adapter = usersFilteredAdapter
                    participantsList.adapter = usersFilteredAdapter


                    usersViewModel.data.observe(viewLifecycleOwner) {
                        val likerOwnerIds = event.likeOwnerIds.orEmpty().toSet()
                        if (likerOwnerIds.isNotEmpty()) {
                            likersListShort.visibility = View.VISIBLE
                            moreLikersButton.visibility = View.VISIBLE
                            likerOwnerIds.forEach { likerOwnerId ->
                                val filteredUsers =
                                    it.users.filter { it.id == likerOwnerId.toLong() }
                                usersFilteredAdapter.submitList(filteredUsers)
                            }
                        }
                    }

                    usersViewModel.data.observe(viewLifecycleOwner) {
                        val speakerOwnerIds = event.speakerIds.orEmpty().toSet()
                        if (speakerOwnerIds.isNotEmpty()) {
                            speakersList.visibility = View.VISIBLE
                            speakersTitle.visibility = View.VISIBLE
                        }
                        speakerOwnerIds.forEach { speakerOwnerId ->
                            val filteredUsers = it.users.filter { it.id == speakerOwnerId.toLong() }
                            usersFilteredAdapter.submitList(filteredUsers)
                        }
                    }

                    usersViewModel.data.observe(viewLifecycleOwner) {
                        val participantOwnerIds = event.participantsIds.orEmpty().toSet()
                        if (participantOwnerIds.isNotEmpty()) {
                            participantsList.visibility = View.VISIBLE
                            participantOwnerIds.forEach { participantOwnerId ->
                                val filteredUsers =
                                    it.users.filter { it.id == participantOwnerId.toLong() }
                                usersFilteredAdapter.submitList(filteredUsers)
                            }
                        }
                    }

                    moreLikersButton.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_fragmentEventInDetails2_to_fragmentLikersEvent2,
                            Bundle().apply
                            { putSerializable("eventKey", event) })
                    }

                    if (event.coordinates == null) {
                        mapView?.findViewById<MapView>(R.id.eventInDetailsMapView)?.onStop()
                        return@observe
                    }

                    mapView?.findViewById<MapView>(R.id.eventInDetailsMapView)?.isVisible =
                        event.coordinates != null

                    MapKitFactory.initialize(requireContext())
                    mapView?.findViewById<MapView>(R.id.eventInDetailsMapView)

                    val latitude = event.coordinates?.latitude!!.toDouble()
                    val longitude = event.coordinates.longitude!!.toDouble()

                    CameraPosition(
                        Point(latitude, longitude),
                        /* zoom = */ 17.0f,
                        /* azimuth = */ 150.0f,
                        /* tilt = */ 30.0f
                    )

                    val imageProvider =
                        ImageProvider.fromResource(requireContext(), R.drawable.map_point)
                    val placemark = mapView?.map?.mapObjects?.addPlacemark().apply {
                        this?.geometry = Point(latitude, longitude)
                        this?.setIcon(imageProvider)
                    }

                    val placemarkTapListener = MapObjectTapListener { _, _ ->
                        Toast.makeText(
                            context,
                            "That's place of doing event",
                            Toast.LENGTH_LONG
                        ).show()
                        true
                    }

                    placemark?.addTapListener(placemarkTapListener)
                    placemark?.removeTapListener(placemarkTapListener)
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}