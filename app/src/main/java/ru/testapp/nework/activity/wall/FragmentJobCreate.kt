package ru.testapp.nework.activity.wall

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.databinding.FragmentJobCreateBinding
import ru.testapp.nework.utils.HideKeyBoardUtil

import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentJobCreate : Fragment() {

    private val viewModel: ViewModelJobs by viewModels()

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobCreateBinding.inflate(inflater, container, false)

        binding.buttonCreateJob.setOnClickListener {
            viewModel.saveJob()
            HideKeyBoardUtil.hideKeyBoard(requireView())
        }

        viewModel.jobCreated.observe(viewLifecycleOwner) {
            viewModel.loadJobsMy()
            findNavController().navigateUp()
        }

        dialog = Dialog(requireContext())

        binding.dateLayout.setOnClickListener {
            dialog.setContentView(R.layout.card_enter_dates)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)

            dialog.show()
        }

//        val startDate = dialog.findViewById<TextInputLayout>(R.id.selectStartDate)
//        val endDate = dialog.findViewById<TextInputLayout>(R.id.selectEndDate)
//
//        binding.startDataText.text = startDate.toString()
//        binding.finishDataText.text = endDate.toString()

        viewModel.startDateState.observe(viewLifecycleOwner) {
            binding.startDataText.text = it
        }

        viewModel.endDateState.observe(viewLifecycleOwner) {
            binding.finishDataText.text = it
        }

        return binding.root
    }
}