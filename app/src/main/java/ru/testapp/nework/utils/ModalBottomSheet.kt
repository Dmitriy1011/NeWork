package ru.testapp.nework.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.testapp.nework.databinding.CardBottomSheetDialogBinding

class ModalBottomSheet(
) : BottomSheetDialogFragment() {

    private lateinit var buttonText: String
    private lateinit var date: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardBottomSheetDialogBinding.inflate(inflater, container, false)

        binding.radioButtonOnline.isChecked = true

        date = binding.dateField.toString()
        setFragmentResult("dateRequestKey", bundleOf("dateBundleKey" to date))

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.radioButtonOnline.id) {
                buttonText = binding.radioButtonOnline.text.toString()
                setFragmentResult("typeRequestKey", bundleOf("typeBundleKey" to buttonText))
                return@setOnCheckedChangeListener
            } else if (checkedId == binding.radioButtonOffline.id) {
                buttonText = binding.radioButtonOffline.text.toString()
                setFragmentResult("typeRequestKey", bundleOf("typeBundleKey" to buttonText))
            }
        }


        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
        var Bundle.dateArg: String? by EventDateArg
        var Bundle.eventTypeArg: String? by EventTypeArg
    }
}