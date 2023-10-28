package ru.testapp.nework.activity.authentication

import android.app.Activity
import android.net.Uri
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.databinding.SignUpLoadProfileImageBinding
import ru.testapp.nework.dto.MediaUpload
import ru.testapp.nework.dto.PhotoModel
import ru.testapp.nework.viewmodel.ViewModelAuth
import ru.testapp.nework.viewmodel.ViewModelPost
import ru.testapp.nework.viewmodel.ViewModelSignUp

@AndroidEntryPoint
class FragmentSignInProfilePhoto : Fragment() {

    private val signUpViewModel: ViewModelSignUp by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SignUpLoadProfileImageBinding.inflate(inflater, container, false)


//        binding.clearPhoto.setOnClickListener {
//            signUpViewModel.clearImage()
//        }

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = requireNotNull(it.data?.data)
                    signUpViewModel.setRegisterImage(
                        MediaUpload(file = uri.toFile())
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.picking_photo_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        signUpViewModel.registerImageState.observe(viewLifecycleOwner) { media ->
            if (media == null) {
                return@observe
            }
            binding.signInProfilePhoto.setImageURI(Uri.parse(media.file.toString()))
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