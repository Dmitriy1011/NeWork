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
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.posts.FragmentCreateAndEditPost.Companion.editTextArg
import ru.testapp.nework.databinding.FragmentEventCreateAndEditBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.utils.EditTextArg
import ru.testapp.nework.utils.EditTextArgEvent
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.utils.ModalBottomSheet
import ru.testapp.nework.viewmodel.ViewModelEvents

@AndroidEntryPoint
class FragmentCreateAndEditEvent : Fragment() {

    private val viewModel: ViewModelEvents by viewModels()

    private lateinit var modalBottomSheet: ModalBottomSheet
    private lateinit var dialog: Dialog

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
                            binding.eventEditText.toString(),
                            binding.displayEventDateTextButton.text.toString(),
                            binding.displayEventTypeButton.text.toString()
                        )
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
                binding.eventClearPreview.isGone = true
                return@observe
            }

            binding.eventClearPreview.isVisible = true
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

        dialog = Dialog(requireContext())
        modalBottomSheet = ModalBottomSheet()

        binding.fab.setOnClickListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)
        }

        setFragmentResultListener("typeRequestKey") { _, bundle ->
            val typeResult = bundle.getString("typeBundleKey")
            binding.displayEventTypeButton.text = typeResult!!
        }
        setFragmentResultListener("dateRequestKey") { _, bundle ->
            val dateResult = bundle.getString("dateBundleKey")
            binding.displayEventDateTextButton.text = dateResult!!
        }

        return binding.root
    }

    companion object {
        var Bundle.editTextArgEvent: String? by EditTextArgEvent
    }
}