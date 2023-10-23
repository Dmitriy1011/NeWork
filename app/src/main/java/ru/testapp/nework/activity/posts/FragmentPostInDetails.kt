package ru.testapp.nework.activity.posts

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
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.adapter.AdapterUsersFilteredPost
import ru.testapp.nework.adapter.OnIteractionListener
import ru.testapp.nework.adapter.OnIteractionListenerUsersFilteredPost
import ru.testapp.nework.adapter.PostsAdapter
import ru.testapp.nework.databinding.FragmentPostInDetailsBinding
import ru.testapp.nework.dto.Post
import ru.testapp.nework.utils.SeparateIdPostArg
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentPostInDetails : Fragment() {

    private val viewModel: ViewModelUsers by viewModels()
    private val viewModelPosts: ViewModelPost by viewModels()

    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostInDetailsBinding.inflate(inflater, container, false)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_post_in_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        })

        viewModelPosts.postData.observe(viewLifecycleOwner) { modelPost ->
            modelPost.posts.find { it.id == arguments?.idArg }?.let { post ->
                binding.cardPostInDetails.apply {
                    postLikersListShortInDetails.visibility = View.VISIBLE
                    moreLikersButton.visibility = View.VISIBLE
                    mentionedTitle.visibility = View.VISIBLE
                    postInDetailsMentionedButton.visibility = View.VISIBLE
                    likersTitle.visibility = View.VISIBLE
                    mentionedListShort.visibility = View.VISIBLE
                    postInDetailsMapView.visibility = View.VISIBLE
                    postShareButton.visibility = View.GONE

                    PostsAdapter.PostViewHolder(this, object : OnIteractionListener {
                        override fun onEdit(post: Post) {
                            viewModelPosts.editPost(post)
                        }

                        override fun onRemove(post: Post) {
                            viewModelPosts.removePost(post.id)
                        }

                        override fun onLike(post: Post) {
                            viewModelPosts.likePost(post.id)
                        }

                        override fun onUnLike(post: Post) {
                            viewModelPosts.unLikePost(post.id)
                        }

                        override fun onOpenImage(post: Post) {
                            findNavController().navigate(
                                R.id.action_fragmentPostInDetails_to_fragmentAttachmentSeparate,
                                Bundle().apply {
                                    textArg = "${BuildConfig.BASE_URL}media/${post.attachment?.url}"
                                }
                            )
                        }

                        override fun onOpenVideo(post: Post) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                            val chooserIntent = Intent.createChooser(
                                intent,
                                getString(R.string.choose_where_open_your_video)
                            )
                            startActivity(chooserIntent)
                        }

                        override fun onOpenAudio(post: Post) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                            val chooserIntent = Intent.createChooser(
                                intent,
                                getString(R.string.choose_where_open_your_audio)
                            )
                            startActivity(chooserIntent)
                        }

                        override fun followingTheLink(post: Post) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
                            startActivity(intent)
                        }
                    })

                    requireActivity().addMenuProvider(object : MenuProvider {
                        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                            menuInflater.inflate(R.menu.menu_post_in_details, menu)
                        }

                        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
                    }, viewLifecycleOwner)

                    val usersFilteredAdapter =
                        AdapterUsersFilteredPost(object : OnIteractionListenerUsersFilteredPost {
                            override fun returnPostForTransfer(post: Post) {
                                moreLikersButton.setOnClickListener {
                                    findNavController().navigate(R.id.action_fragmentPostInDetails_to_fragmentPostLikers,
                                        Bundle().apply {
                                            putSerializable("postKey", post)
                                        }
                                    )
                                }
                            }
                        })

                    postLikersListShortInDetails.adapter = usersFilteredAdapter
                    mentionedListShort.adapter = usersFilteredAdapter

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
                            val filteredUsers =
                                it.users.filter { it.id == mentionedOwnerId.toLong() }
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

    companion object {
        var Bundle.idArg: Long by SeparateIdPostArg
    }
}
