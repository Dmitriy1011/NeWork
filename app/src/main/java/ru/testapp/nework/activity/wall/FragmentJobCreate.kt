package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.databinding.FragmentJobCreateBinding
import ru.testapp.nework.utils.HideKeyBoardUtil
import ru.testapp.nework.viewmodel.ViewModelJobs

@AndroidEntryPoint
class FragmentJobCreate : Fragment() {

    private val viewModel: ViewModelJobs by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobCreateBinding.inflate(inflater, container, false)

        binding.buttonCreateJob.setOnClickListener {
            viewModel.changeContent(
                binding.jobCreateNameFieldInput.text.toString(),
                binding.jobCreatePositionFieldInput.text.toString(),
                binding.jobCreateLinkFieldInput.text.toString(),
                binding.startDataText.text.toString(),
                binding.finishDataText.text.toString()
            )
            viewModel.saveJob()
            HideKeyBoardUtil.hideKeyBoard(requireView())
        }

        viewModel.jobCreated.observe(viewLifecycleOwner) {
            viewModel.loadJobsMy()
            findNavController().navigateUp()
        }

        val dialog = FragmentEnterDate()

        viewModel.startDateState.observe(viewLifecycleOwner) { startDate ->
            binding.startDataText.text = startDate
        }

        viewModel.endDateState.observe(viewLifecycleOwner) { endDate ->
            binding.finishDataText.text = endDate
        }

        binding.dateLayout.setOnClickListener {
            dialog.show(requireActivity().supportFragmentManager, "FragmentEnterDate")
        }
        return binding.root
    }
}