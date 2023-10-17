package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import ru.testapp.nework.R
import ru.testapp.nework.adapter.AdapterLikersShortList
import ru.testapp.nework.databinding.FragmentPostInDetailsBinding
import ru.testapp.nework.dto.Post

class FragmentPostInDetails(
    private val post: Post
) : Fragment() {

    private var mapView: MapView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentPostInDetailsBinding.bind(view)

        mapView?.findViewById<MapView>(R.id.postInDetailsMapView)?.isVisible = post.coordinates != null

        val likersAdapter = AdapterLikersShortList()

        binding.likersListShort.adapter = likersAdapter

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