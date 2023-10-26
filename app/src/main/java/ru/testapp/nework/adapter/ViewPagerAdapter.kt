package ru.testapp.nework.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.testapp.nework.activity.wall.FragmentJobsMy
import ru.testapp.nework.activity.wall.FragmentWallMy

class ViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentWallMy()
            1 -> FragmentJobsMy()
            else -> FragmentWallMy()
        }
    }

    override fun getItemCount(): Int {
        return 0
    }

}