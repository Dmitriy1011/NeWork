package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
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

            viewModel.registerImage.observe(viewLifecycleOwner) { media ->
                viewModel.saveRegisteredUser(login, pass, name, media.file)
            }

            authViewModel.data.observe(viewLifecycleOwner) { state ->
                appAuth.setAuth(state.id, state.token!!)
            }
        }

        findNavController().navigateUp()

        return binding.root
    }
}