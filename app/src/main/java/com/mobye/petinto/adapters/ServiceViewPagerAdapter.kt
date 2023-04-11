package com.mobye.petinto.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobye.petinto.ui.fragments.HotelFragment
import com.mobye.petinto.ui.fragments.SpaFragment

class ServiceViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return SpaFragment()
            1-> return HotelFragment()
            else -> {
                return Fragment()
            }
        }
    }

}