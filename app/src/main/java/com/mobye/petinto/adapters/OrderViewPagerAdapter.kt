package com.mobye.petinto.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobye.petinto.ui.fragments.HistoryInformation
import com.mobye.petinto.ui.fragments.HistoryPetFragment

class OrderViewPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HistoryInformation()
            1-> HistoryPetFragment()
            else -> {
                HistoryInformation()
            }
        }
    }

}