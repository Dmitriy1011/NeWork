package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val viewModel: ViewModelSignIn by activityViewModels()
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

            binding.progressBar.isVisible = true

            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authenticated) {
                    findNavController().navigateUp()
                }
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignIn_to_fragmentSignUp)
        }

       return binding.root
    }
}