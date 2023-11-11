package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val login = binding.loginTextFieldInput.text.toString()
            val pass = binding.passwordTextFieldInput.text.toString()

            viewModel.saveIdAndToken(login, pass)

            viewModel.wrongDataErrorState.observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it.loading
                if (it.error) {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.incorrect_login_or_password),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authenticated) {
                    findNavController().navigateUp()
                } else {
                    binding.passwordTextField.isHelperTextEnabled = true
                }
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
        }

        return binding.root
    }
}