package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentSignInBinding
import ru.testapp.nework.viewmodel.ViewModelAuth
import ru.testapp.nework.viewmodel.ViewModelSignIn
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSignIn : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth

    private val viewModel: ViewModelSignIn by viewModels()
    private val authViewModel: ViewModelAuth by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val login = binding.loginTextField.editText?.text.toString()
            val pass = binding.passwordTextField.editText?.text.toString()
            viewModel.saveIdAndToken(login, pass)

            findNavController().navigateUp()
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
        }

       return binding.root
    }
}