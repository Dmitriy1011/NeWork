package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.testapp.nework.R
import ru.testapp.nework.adapter.ViewPagerAdapter
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentProfileMyBinding
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfileMy : Fragment(R.layout.fragment_profile_my) {
    @Inject
    lateinit var appAuth: AppAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentProfileMyBinding.bind(view)

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

        val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager2.adapter = viewPagerAdapter

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

        binding.fab.setOnClickListener {
            if (appAuth.authStateFlow.value.token != null) {
                findNavController().navigate(R.id.action_fragmentProfileMy_to_fragmentJobCreate)
            }
            Snackbar.make(
                binding.root,
                getString(R.string.fab_click_message_job),
                Snackbar.LENGTH_LONG
            ).setAction(
                getString(R.string.sign_in),
            ) {
                findNavController().navigate(R.id.action_fragmentProfileMy_to_fragmentSignIn)
            }.show()
        }
    }
}