package ru.testapp.nework.activity.posts

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.testapp.nework.R
import ru.testapp.nework.databinding.FragmentPostCreateAndEditBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.utils.EditTextArg
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.viewmodel.ViewModelPost

@AndroidEntryPoint
class FragmentCreateAndEditPost : Fragment() {
    private val viewModel: ViewModelPost by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostCreateAndEditBinding.inflate(inflater, container, false)

        arguments?.editTextArg?.let(binding.editText::setText)

        binding.peopleButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCreateAndEditPost_to_fragmentChooseUsers)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.saveNewPost -> {
                        viewModel.changeContent(binding.editText.toString())
                        viewModel.savePost()
                        HideKeyBoardUtil.hideKeyBoard(requireView())
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }


        binding.clearPreview.setOnClickListener {
            viewModel.photoState.observe(viewLifecycleOwner) { photoModel ->
                if (photoModel != null) {
                    viewModel.clearPhoto()
                }
            }
        }

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = requireNotNull(it.data?.data)
                    viewModel.setPhoto(
                        PhotoModel(uri = uri, file = uri.toFile())
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.picking_photo_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        viewModel.photoState.observe(viewLifecycleOwner) { photoModel ->
            if (photoModel == null) {
                binding.previewImage.visibility = View.GONE
                binding.clearPreview.visibility = View.GONE
                return@observe
            }

            binding.clearPreview.visibility = View.VISIBLE
            binding.previewImage.visibility = View.VISIBLE
            binding.previewImage.setImageURI(photoModel.uri)
        }

        binding.addPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.loadPhotoFromGalery.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        return binding.root
    }

    companion object {
        var Bundle.editTextArg: String? by EditTextArg
    }
}