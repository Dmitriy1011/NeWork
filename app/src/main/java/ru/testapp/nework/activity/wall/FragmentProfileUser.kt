package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.adapter.ViewPagerAdapterUser
import ru.testapp.nework.databinding.FragmentProfileMyBinding

@AndroidEntryPoint
class FragmentProfileUser : Fragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var viewPagerAdapterUser: ViewPagerAdapterUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileMyBinding.inflate(inflater, container, false)

        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        viewPager2 = requireActivity().findViewById(R.id.viewPager)
        viewPager2.adapter = viewPagerAdapterUser
        viewPagerAdapterUser = ViewPagerAdapterUser(this)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })
        return binding.root
    }
}