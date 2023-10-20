package ru.testapp.nework.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.testapp.nework.activity.wall.FragmentJobsUser
import ru.testapp.nework.activity.wall.FragmentWallUser

class ViewPagerAdapterUser(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val result = when (position) {
            0 -> FragmentWallUser()
            1 -> FragmentJobsUser()
            else -> FragmentWallUser()
        }
        return result
    }

    override fun getItemCount(): Int {
        return 0
    }
}