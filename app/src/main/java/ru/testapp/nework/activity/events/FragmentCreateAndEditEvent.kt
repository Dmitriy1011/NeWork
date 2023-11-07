package ru.testapp.nework.activity.events

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentCreateAndEditPost.Companion.editTextArg
import ru.testapp.nework.databinding.FragmentEventCreateAndEditBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.viewmodel.ViewModelEvents

@AndroidEntryPoint
class FragmentCreateAndEditEvent : Fragment() {

    private val viewModel: ViewModelEvents by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventCreateAndEditBinding.inflate(inflater, container, false)

        arguments?.editTextArg?.let(binding.eventEditText::setText)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_event, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.saveNewEvent -> {
                        viewModel.changeContent(
                            binding.eventEditText.text?.toString().orEmpty(),
                            binding.displayEventDateTextButton.text?.toString().orEmpty(),
                            binding.displayEventTypeButton.text?.toString().orEmpty()
                        )
                        viewModel.saveEvent()
                        HideKeyBoardUtil.hideKeyBoard(requireView())
                        findNavController().navigate(R.id.action_fragmentCreateAndEditEvent_to_fragmentEvents)
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
                binding.eventPreviewImage.visibility = View.GONE
                binding.eventClearPreview.visibility = View.GONE
                return@observe
            }

            binding.eventClearPreview.visibility = View.VISIBLE
            binding.eventPreviewImage.visibility = View.VISIBLE
            binding.eventPreviewImage.setImageURI(photoModel.uri)
        }

        binding.eventClearPreview.setOnClickListener {
            viewModel.photoState.observe(viewLifecycleOwner) { photoModel ->
                if (photoModel != null) {
                    viewModel.clearPhoto()
                }
            }
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

        val dialog = Dialog(requireContext())
        val modalBottomSheet = ModalBottomSheet()

        binding.fab.setOnClickListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)
        }

        viewModel.eventTypesState.observe(viewLifecycleOwner) {
            binding.displayEventTypeButton.text = it
        }

        viewModel.dateTimeState.observe(viewLifecycleOwner) {
            binding.displayEventDateTextButton.text = it
        }

        return binding.root
    }
}