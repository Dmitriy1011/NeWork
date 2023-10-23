package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.users.FragmentUsers.Companion.userIdArg
import ru.testapp.nework.adapter.ViewPagerAdapterUser
import ru.testapp.nework.databinding.FragmentProfileMyBinding
import ru.testapp.nework.databinding.FragmentProfileUserBinding
import ru.testapp.nework.handler.loadAvatarImage
import ru.testapp.nework.viewmodel.ViewModelUsers

@AndroidEntryPoint
class FragmentProfileUser : Fragment(R.layout.fragment_profile_user) {

    private val viewModel: ViewModelUsers by viewModels()

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapterUser: ViewPagerAdapterUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProfileUserBinding.bind(view)

        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        viewPager2 = requireActivity().findViewById(R.id.viewPager)
        viewPager2.adapter = viewPagerAdapterUser
        viewPagerAdapterUser = ViewPagerAdapterUser(this)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
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

        viewModel.data.observe(viewLifecycleOwner) { modelUser ->
            modelUser.users.find { it.id == arguments?.userIdArg }.let { user ->
                binding.apply {
                    profileUserImage.loadAvatarImage(user?.avatar!!)
                    requireActivity().findViewById<Toolbar>(R.id.toolbar).title = user.name
                }
            }
        }
    }
}