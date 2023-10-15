package ru.testapp.nework.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.testapp.nework.databinding.FragmentSignUpBinding
import ru.testapp.nework.viewmodel.ViewModelSignUp

class FragmentSignUp : Fragment() {

    private val viewModel: ViewModelSignUp by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSignUpBinding.bind(view)

        binding.signUpButton.setOnClickListener {
            val name = binding.nameTextField.editText?.text.toString()
            val login = binding.regLoginTextField.editText?.text.toString()
            val pass = binding.regPasswordTextField.editText?.text.toString()

            viewModel.registerImage.observe(viewLifecycleOwner) { media ->
                viewModel.saveRegisteredUser(login, pass, name, media.file)
            }
        }

        findNavController().navigateUp()
    }
}