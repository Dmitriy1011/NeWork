package ru.testapp.nework.activity.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.databinding.CardBottomSheetDialogBinding
import ru.testapp.nework.viewmodel.ViewModelEvents

@AndroidEntryPoint
class ModalBottomSheet(
) : BottomSheetDialogFragment() {

    private val viewModel: ViewModelEvents by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardBottomSheetDialogBinding.inflate(inflater, container, false)

        binding.radioButtonOnline.isChecked = true

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButtonOnline.id -> viewModel.editType(binding.radioButtonOnline.text.toString())
                binding.radioButtonOffline.id -> viewModel.editType(binding.radioButtonOffline.text.toString())
            }
        }

        requireNotNull(binding.dateField.editText).doAfterTextChanged {
            viewModel.editDateTime(it?.toString().orEmpty())
        }

        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}