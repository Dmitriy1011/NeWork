package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.users.FragmentUsers.Companion.userIdArg
import ru.testapp.nework.adapter.ViewPagerAdapterUser
import ru.testapp.nework.databinding.FragmentProfileUserBinding
import ru.testapp.nework.handler.loadImage
import ru.testapp.nework.viewmodel.ViewModelJobs
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentProfileUser : Fragment(R.layout.fragment_profile_user) {

    private val viewModel: ViewModelUsers by viewModels()
    private val viewModelJobs: ViewModelJobs by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProfileUserBinding.bind(view)

        val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = binding.viewPagerUsers
        val viewPagerAdapterUser = ViewPagerAdapterUser(this)
        viewPager2.adapter = viewPagerAdapterUser


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })

        val userIdArg = arguments?.let { it.userIdArg }

        viewModel.data.observe(viewLifecycleOwner) { modelUser ->
            modelUser.users.find { it.id == userIdArg }.let { user ->
                binding.apply {
                    profileUserImage.loadImage(user?.avatar!!)
                    val toolbar = Toolbar(requireContext())
                    toolbar.title = user.name
                }
                viewModelJobs.setUserIdState(user?.id!!.toLong())
            }
        }
    }
}