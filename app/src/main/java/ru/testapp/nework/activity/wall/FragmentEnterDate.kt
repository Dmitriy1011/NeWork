package ru.testapp.nework.activity.wall

import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardEnterDatesBinding
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentEnterDate : DialogFragment() {

    private val viewModel: ViewModelJobs by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
            val binding = CardEnterDatesBinding.inflate(layoutInflater)

            requireNotNull(binding.selectStartDate.editText).doAfterTextChanged { startEditable ->
                viewModel.editStartDate(startEditable?.toString().orEmpty())
            }

            requireNotNull(binding.selectEndDate.editText).doAfterTextChanged { endEditable ->
                viewModel.editEndDate(endEditable?.toString().orEmpty())
            }

            builder
                .setView(binding.root).let {
                    setStyle(
                        R.style.AlertDialog_AppCompat_,
                        R.style.AlertDialog_AppCompat_
                    )

                    binding.apply {
                        okButton.setOnClickListener {
                            dialog?.cancel()
                        }

                        cancelButton.setOnClickListener {
                            dialog?.cancel()
                        }
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}