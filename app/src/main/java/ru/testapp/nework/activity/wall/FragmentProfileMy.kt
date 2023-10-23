package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.activity.users.FragmentUsers.Companion.userIdArg
import ru.testapp.nework.adapter.ViewPagerAdapter
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentProfileMyBinding
import ru.testapp.nework.viewmodel.ViewModelUsers
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfileMy : Fragment(R.layout.fragment_profile_my) {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var viewPagerAdapter: ViewPagerAdapter

    @Inject
    lateinit var appAuth: AppAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FragmentProfileMyBinding.bind(view)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_profile_my, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.logout -> {
                        appAuth.removeAuth()
                        true
                    }

                    else -> false
                }
        }, viewLifecycleOwner)

        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        viewPager2 = requireActivity().findViewById(R.id.viewPager)
        viewPager2.adapter = viewPagerAdapter
        viewPagerAdapter = ViewPagerAdapter(this)

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
    }
}