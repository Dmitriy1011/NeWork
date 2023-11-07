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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.BuildConfig
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentPostsFeed.Companion.textArg
import ru.testapp.nework.adapter.AdapterUsersFilteredPost
import ru.testapp.nework.adapter.OnIteractionListener
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

    private var mapView: MapView = MapView(requireContext())

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
        }, viewLifecycleOwner)

        val currentMenuProvider: MenuProvider? = null

        val arg = arguments?.let { it.idArg }

        viewModelPosts.postData.observe(viewLifecycleOwner) { modelPost ->
            modelPost.posts.map { arg?.let { longTypeId -> it.copy(id = longTypeId) } }
            currentMenuProvider?.let { requireActivity().removeMenuProvider(currentMenuProvider) }
            modelPost.posts.find { it.id == arg }?.let { post ->
                binding.cardPostInDetails.apply {
                    postLikersListShortInDetails.visibility = View.VISIBLE
                    likersTitle.visibility = View.VISIBLE
                    postInDetailsMapView.visibility = View.VISIBLE
                    mentionedTitle.visibility = View.VISIBLE
                    postInDetailsMentionedButton.visibility = View.VISIBLE

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
                    }).bind(post)

                    postMenuButton.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.edit -> {
                                        viewModelPosts.editPost(post)
                                        true
                                    }

                                    R.id.delete -> {
                                        viewModelPosts.removePost(post.id)
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }.show()
                    }

                    requireActivity().addMenuProvider(object : MenuProvider {
                        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                            menuInflater.inflate(R.menu.menu_post_in_details, menu)
                        }

                        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
                    }, viewLifecycleOwner)

                    val usersFilteredAdapter = AdapterUsersFilteredPost()

                    moreLikersButton.setOnClickListener {
                        findNavController().navigate(R.id.action_fragmentPostInDetails_to_fragmentPostLikers,
                            Bundle().apply {
                                putSerializable("postKey", post)
                            }
                        )
                    }

                    postLikersListShortInDetails.adapter = usersFilteredAdapter
                    mentionedListShort.adapter = usersFilteredAdapter

                    viewModel.data.observe(viewLifecycleOwner) {
                        val likerOwnersIds = post.likeOwnerIds.orEmpty().toSet()
                        if (likerOwnersIds.isNotEmpty()) {
                            moreLikersButton.visibility = View.VISIBLE
                            likerOwnersIds.forEach { likerOwnerId ->
                                val filteredUsers =
                                    it.users.filter { it.id == likerOwnerId.toLong() }
                                usersFilteredAdapter.submitList(filteredUsers)
                            }
                        }
                    }

                    viewModel.data.observe(viewLifecycleOwner) {
                        val mentionedOwnersIds = post.mentionIds.orEmpty().toSet()
                        if (mentionedOwnersIds.isNotEmpty()) {
                            mentionedListShort.visibility = View.VISIBLE
                            mentionedOwnersIds.forEach { mentionedOwnerId ->
                                val filteredUsers =
                                    it.users.filter { it.id == mentionedOwnerId.toLong() }
                                usersFilteredAdapter.submitList(filteredUsers)
                            }
                        }
                    }

                    if (post.coordinates == null) {
                        mapView?.findViewById<MapView>(R.id.postInDetailsMapView)?.onStop()
                        return@observe
                    }

                    mapView?.findViewById<MapView>(R.id.postInDetailsMapView)?.isVisible =
                        true

                    MapKitFactory.initialize(requireContext())
                    mapView?.findViewById<MapView>(R.id.postInDetailsMapView)

                    val latitude = post.coordinates.latitude.toDouble()
                    val longitude = post.coordinates.longitude.toDouble()

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

    override fun onDestroy() {
        mapView = null
        super.onDestroy()
    }

    companion object {
        var Bundle.idArg: Long by SeparateIdPostArg
    }
}
