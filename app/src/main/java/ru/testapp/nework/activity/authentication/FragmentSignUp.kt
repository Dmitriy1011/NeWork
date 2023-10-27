package ru.testapp.nework.activity.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val viewModel: ViewModelSignUp by activityViewModels()
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

            binding.progressBar.isVisible = true

            authViewModel.data.observe(viewLifecycleOwner) {
                if (authViewModel.authenticated) {
                    findNavController().navigateUp()
                }
            }
        }

        return binding.root
    }
}