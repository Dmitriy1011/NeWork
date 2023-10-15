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
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.testapp.nework.R
import ru.testapp.nework.databinding.FragmentEventCreateAndEditBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.utils.ModalBottomSheet
import ru.testapp.nework.viewmodel.ViewModelEvents

class FragmentCreateAndEditEvent : Fragment() {

    private val viewModel: ViewModelEvents by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentEventCreateAndEditBinding.bind(view)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_event, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.savePost -> {
                        viewModel.changeContent(binding.eventEditText.toString())
                        viewModel.saveEvent()
                        HideKeyBoardUtil.hideKeyBoard(requireView())
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

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
                binding.eventPreviewImage.isGone = true
                return@observe
            }

            binding.eventPreviewImage.isVisible = true
            binding.eventPreviewImage.setImageURI(photoModel.uri)
        }

        binding.eventAddPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.eventLoadPhotoFromGalery.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        val modalBottomSheet = ModalBottomSheet()

        binding.fab.setOnClickListener {
            modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)
        }
    }
}