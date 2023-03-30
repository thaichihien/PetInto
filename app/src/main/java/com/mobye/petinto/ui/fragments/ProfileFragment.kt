package com.mobye.petinto.ui.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.mobye.petinto.R
import com.mobye.petinto.adapters.CarouselAdapter
import com.mobye.petinto.adapters.OrderAdapter
import com.mobye.petinto.databinding.FragmentProfileBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    val DEBUG_TAG = "ProfileFragment"
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val carouselViewModel : InformationViewModel by activityViewModels {
        InformationViewModelFactory(InformationRepository())
    }

    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(DEBUG_TAG,"onViewCreated")

        binding.apply {
            navOrder.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOrderFragment())
            }
            navBooking.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToBookingFragment())
            }
            navHistory.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHistoryInformation())
            }
            btnAdd.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAddPetFragment())
            }
        }

        val viewPage = binding.viewPagerCarousel

        viewPage.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }

        carouselAdapter = CarouselAdapter()

//        carouselViewModel.getOrderList()
        carouselViewModel.myPetList.observe(viewLifecycleOwner){
            carouselAdapter.differ.submitList(it)
        }

        binding.apply {
            viewPage.apply {
                adapter = carouselAdapter
            }
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        viewPage.setPageTransformer(compositePageTransformer)
    }


    override fun onPause() {
        super.onPause()
        Log.e(DEBUG_TAG,"onPause")
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}