package az.zero.instabugtaskaz.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import az.zero.instabugtaskaz.R
import az.zero.instabugtaskaz.presentation.adapters.SliderAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpSlider()
    }

    private fun setUpSlider() {
        val sliderAdapter = SliderAdapter(supportFragmentManager, lifecycle)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)
        viewPager2.adapter = sliderAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = sliderAdapter.getTitleByPosition(position)
        }.attach()
    }

}
