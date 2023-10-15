package ru.testapp.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.testapp.nework.R
import ru.testapp.nework.databinding.FragmentPostCreateAndEditBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.viewmodel.ViewModelPost

class FragmentCreateAndEditPost : Fragment() {
    private val viewModel: ViewModelPost by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentPostCreateAndEditBinding.bind(view)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.savePost -> {
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
            viewModel.clearPhoto()
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
                binding.previewImage.isGone = true
                return@observe
            }

            binding.previewImage.isVisible = true
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
    }
}