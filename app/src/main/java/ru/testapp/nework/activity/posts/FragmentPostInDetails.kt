package ru.testapp.nework.activity.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterUsersFilteredEvent
import ru.testapp.nework.databinding.FragmentPostInDetailsBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.utils.EditTextArg
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentPostInDetails(
   private val post: Post
) : Fragment() {

    private val viewModel: ViewModelUsers by viewModels()

    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostInDetailsBinding.inflate(inflater, container, false)

        findNavController().navigate(R.id.fragmentPostInDetails, Bundle().apply {
            putSerializable("postKey", post)
        })

        val post = requireArguments().getSerializable("postKey") as Post

        val usersFilteredAdapter = AdapterUsersFilteredEvent()

        binding.likersListShort.adapter = usersFilteredAdapter
        binding.mentionedListShort.adapter = usersFilteredAdapter

        viewModel.data.observe(viewLifecycleOwner) {
            val likerOwnersIds = post.likeOwnerIds.orEmpty().toSet()
            likerOwnersIds.forEach { likerOwnerId ->
                val filteredUsers = it.users.filter { it.id == likerOwnerId.toLong() }
                usersFilteredAdapter.submitList(filteredUsers)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            val mentionedOwnersIds = post.mentionIds.orEmpty().toSet()
            mentionedOwnersIds.forEach { mentionedOwnerId ->
                val filteredUsers = it.users.filter { it.id == mentionedOwnerId.toLong() }
                usersFilteredAdapter.submitList(filteredUsers)
            }
        }

        mapView?.findViewById<MapView>(R.id.postInDetailsMapView)?.isVisible =
            post.coordinates != null

        MapKitFactory.initialize(requireContext())
        mapView?.findViewById<MapView>(R.id.postInDetailsMapView)

        CameraPosition(
            Point(55.751225, 37.629540),
            /* zoom = */ 17.0f,
            /* azimuth = */ 150.0f,
            /* tilt = */ 30.0f
        )

        val latitude = post.coordinates?.latitude!!.toDouble()
        val longitude = post.coordinates?.longitude!!.toDouble()

        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.map_point)
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