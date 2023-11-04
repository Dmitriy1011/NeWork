package ru.testapp.nework.activity.wall

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.databinding.CardEnterDatesBinding
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentEnterDate : DialogFragment() {

    private val viewModel: ViewModelJobs by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val binding = CardEnterDatesBinding.inflate(layoutInflater)

            builder
                .setView(binding.root).let {
                    setStyle(
                        R.style.AlertDialog_AppCompat_,
                        R.style.AlertDialog_AppCompat_
                    )
                    binding.apply {
                        okButton.setOnClickListener {
                            val startDate = binding.selectStartDateInput.text.toString()
                            val endDate = binding.selectEndDateInput.text.toString()

                            viewModel.editStartDate(startDate)
                            viewModel.editEndDate(endDate)

                            binding.progressBar.visibility = View.VISIBLE

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