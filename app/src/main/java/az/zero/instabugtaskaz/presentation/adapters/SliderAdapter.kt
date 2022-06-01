package az.zero.instabugtaskaz.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import az.zero.instabugtaskaz.presentation.home.HomeFragment
import az.zero.instabugtaskaz.presentation.search.SearchFragment

class SliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList = listOf(
        "Home" to HomeFragment(),
        "Search" to SearchFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    fun getTitleByPosition(position: Int): String = fragmentList[position].first

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].second
    }

}