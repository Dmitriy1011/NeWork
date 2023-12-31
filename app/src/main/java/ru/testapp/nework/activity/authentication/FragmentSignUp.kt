package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentSignUpBinding
import ru.testapp.nework.viewmodel.ViewModelAuth
import ru.testapp.nework.viewmodel.ViewModelSignUp
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSignUp : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: ViewModelSignUp by viewModels()
    private val authViewModel: ViewModelAuth by viewModels()
    private val signUpViewModel: ViewModelSignUp by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener {
            val name = binding.nameTextField.editText?.text.toString()
            val login = binding.regLoginTextField.editText?.text.toString()
            val pass = binding.regPasswordTextField.editText?.text.toString()
            val passConfirm = binding.regConfirmPasswordTextField.editText?.text.toString()

            val media = signUpViewModel.registerImageState.value

            if (pass == passConfirm) {
                if (media != null) {
                    viewModel.saveRegisteredUser(login, pass, name, media.file)
                } else {
                    viewModel.saveRegisterUserWithoutAvatar(login, pass, name)
                }
            } else {
                binding.regConfirmPasswordTextField.isHelperTextEnabled = true
            }

            signUpViewModel.registerImageState.observe(viewLifecycleOwner) { media ->
                Glide.with(this)
                    .load(media?.file)
                    .into(binding.signInProfilePhoto)
            }


            binding.progressBar.isVisible = true

            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authenticated) {
                    findNavController().navigateUp()
                }
            }
        }

        binding.signInProfilePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentSignInProfilePhoto)
        }

        signUpViewModel.registerUserState.observe(viewLifecycleOwner) {
            if (it.error) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.registration_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }
}