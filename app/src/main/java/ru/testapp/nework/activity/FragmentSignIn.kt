package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.databinding.FragmentSignInBinding
import ru.testapp.nework.viewmodel.ViewModelSignIn

@AndroidEntryPoint
class FragmentSignIn : Fragment() {

    private val viewModel: ViewModelSignIn by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSignInBinding.bind(view)

        binding.signInButton.setOnClickListener {
            val login = binding.loginTextField.editText?.text.toString()
            val pass = binding.passwordTextField.editText?.text.toString()
            viewModel.saveIdAndToken(login, pass)
            findNavController().navigateUp()
        }
    }
}