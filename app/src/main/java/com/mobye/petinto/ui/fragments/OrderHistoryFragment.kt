package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mobye.petinto.adapters.OrderViewPagerAdapter
import com.mobye.petinto.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {
    private var _binding : FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: OrderViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = OrderViewPagerAdapter(this)

        val tabLayoutMediator =
            TabLayoutMediator(binding.historyTabLayout, binding.historyViewPager
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = "Product Order"
                    1 -> tab.text = "Pet Order"
                }
            }

        binding.apply {
            historyViewPager.adapter = viewPagerAdapter
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        tabLayoutMediator.attach()
    }
}