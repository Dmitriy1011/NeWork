package ru.testapp.nework.activity.authentication

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    private val signUpViewModel: ViewModelSignUp by viewModels()

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

            signUpViewModel.registerImageState.observe(viewLifecycleOwner) { media ->
                if (media != null) {
                    viewModel.saveRegisteredUser(login, pass, name, media.file)
                    binding.signInProfilePhoto.setImageURI(Uri.parse(media.toString()))
                }
                viewModel.saveRegisterUserWithoutAvatar(login, pass, name)
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

        return binding.root
    }
}