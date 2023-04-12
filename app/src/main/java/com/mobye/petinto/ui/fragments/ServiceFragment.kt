package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobye.petinto.R
import com.mobye.petinto.adapters.ServiceViewPagerAdapter


class ServiceFragment : Fragment(R.layout.fragment_service) {
    var tabLayout : TabLayout? = null
    var viewPager: ViewPager2? = null
    var viewPagerAdapter: ServiceViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_service)
        tabLayout = findViewById(R.id.service_TabLayout)
        viewPager = findViewById(R.id.service_viewPager)
        viewPagerAdapter = ServiceViewPagerAdapter(this)
        viewPager?.adapter = viewPagerAdapter
        val tabLayoutMediator =
            TabLayoutMediator(tabLayout!!, viewPager!!,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when(position) {
                        0 -> tab.text = "Tab1 Title"
                        1 -> tab.text = "Tab2 Title"
                    }
                })
        tabLayoutMediator.attach()
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            //Toast.makeText(this@ServiceFragment, "Tab ${tab?.text} selected", Toast.LENGTH_SHORT).show()
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {
            //Toast.makeText(this@ServiceFragment, "Tab ${tab?.text} unselected", Toast.LENGTH_SHORT).show()
        }
    })
}
}