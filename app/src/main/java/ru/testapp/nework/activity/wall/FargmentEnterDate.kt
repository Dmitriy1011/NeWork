package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.databinding.CardEnterDatesBinding
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FargmentEnterDate : Fragment() {

    private val viewModel: ViewModelJobs by viewModels()

//    private lateinit var startDate: String
//    private lateinit var endDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardEnterDatesBinding.inflate(inflater, container, false)

//        startDate = binding.selectStartDate.toString()
//        endDate = binding.selectEndDate.toString()

//        binding.okButton.setOnClickListener {
//            setFragmentResult("startDateRequestKey", bundleOf("startDateBundleKey" to startDate))
//            setFragmentResult("endDateRequestKey", bundleOf("endDateBundleKey" to endDate))
//        }

        binding.cancelButton.setOnClickListener {
            binding.root.isVisible = false
        }

        binding.okButton.setOnClickListener {
            viewModel.startDateState.observe(viewLifecycleOwner) {
                viewModel.editStartDate(binding.selectStartDate.toString())
            }

            viewModel.endDateState.observe(viewLifecycleOwner) {
                viewModel.editEndDate(binding.selectEndDate.toString())
            }
        }



        return binding.root
    }
}