package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.BookingListItemAdapter
import com.mobye.petinto.databinding.FragmentBookingBinding
import com.mobye.petinto.databinding.FragmentProfileBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel


class BookingFragment : Fragment(R.layout.fragment_booking_detail) {
    val DEBUG_TAG = "BookingFragment"
    private var _binding : FragmentBookingBinding? = null
    private val binding get() = _binding!!


    private lateinit var bookingAdapter : BookingListItemAdapter

    private val serviceViewModel : ServiceViewModel by activityViewModels {
        PetIntoViewModelFactory(ServiceRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loadingLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }


        bookingAdapter = BookingListItemAdapter {
            findNavController().navigate(BookingFragmentDirections.actionBookingFragmentToBookingDetailSpaFragment(it))
        }

        informationViewModel.user.observe(viewLifecycleOwner){
            bookingAdapter.differ.submitList(listOf())
            bookingAdapter.notifyDataSetChanged()
            serviceViewModel.getBookingHistory(it.id)
        }

        serviceViewModel.bookingList.observe(viewLifecycleOwner){
            bookingAdapter.differ.submitList(it)
            binding.loadingLayout.visibility = View.GONE
        }

        binding.apply {
            btnExit.setOnClickListener {
                findNavController().popBackStack()
            }
            rvBookingList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = bookingAdapter
            }


        }
    }
}