package ru.testapp.nework.activity.authentication

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.testapp.nework.R
import ru.testapp.nework.databinding.SignUpLoadProfileImageBinding
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.viewmodel.ViewModelPost

class FragmentSignInProfilePhoto : Fragment() {

    private val viewModel: ViewModelPost by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SignUpLoadProfileImageBinding.inflate(inflater, container, false)

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

                return@observe
            }
            binding.signInProfilePhoto.setImageURI(photoModel.uri)
        }

        binding.cameraAddPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.galeryLoadPhoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(2048)
                .createIntent(pickPhotoLauncher::launch)
        }

        return binding.root
    }
}