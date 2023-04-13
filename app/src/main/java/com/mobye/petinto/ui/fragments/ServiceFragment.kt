package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobye.petinto.R
import com.mobye.petinto.adapters.ServiceViewPagerAdapter
import com.mobye.petinto.databinding.FragmentOrderPaymentBinding
import com.mobye.petinto.databinding.FragmentServiceBinding


class ServiceFragment : Fragment(R.layout.fragment_service) {

    private var _binding : FragmentServiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ServiceViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ServiceViewPagerAdapter(this)

        val tabLayoutMediator =
            TabLayoutMediator(binding.serviceTabLayout, binding.serviceViewPager
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = "Spa Booking"
                    1 -> tab.text = "Hotel Booking"
                }
            }

        binding.apply {
            serviceViewPager.adapter = viewPagerAdapter
        }

        tabLayoutMediator.attach()

    }
}