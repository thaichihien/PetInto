package com.mobye.petinto.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobye.petinto.ui.fragments.HotelFragment
import com.mobye.petinto.ui.fragments.SpaFragment

class ServiceViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SpaFragment()
            1-> HotelFragment()
            else -> {
                SpaFragment()
            }
        }
    }
}