package ru.testapp.nework.activity.wall

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.testapp.nework.R
import ru.testapp.nework.activity.users.FragmentUsers.Companion.userIdArg
import ru.testapp.nework.adapter.ViewPagerAdapter
import ru.testapp.nework.auth.AppAuth
import ru.testapp.nework.databinding.FragmentProfileMyBinding
import ru.testapp.nework.handler.loadImage
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

        tabLayout = requireActivity().findViewById(R.id.tabLayout)
        viewPager2 = requireActivity().findViewById(R.id.viewPager)
        viewPagerAdapter = ViewPagerAdapter(this)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.fab.setOnClickListener {
                    if (appAuth.authStateFlow.value != null) {
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
    }
}