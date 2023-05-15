package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
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
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel

class BookingFragment : Fragment(R.layout.fragment_booking_detail) {
    val DEBUG_TAG = "BookingFragment"
    private var _binding : FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookingAdapter : BookingListItemAdapter
    private lateinit var currentBooking : Booking

    private val serviceViewModel : ServiceViewModel by activityViewModels {
        PetIntoViewModelFactory(ServiceRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.dialog
    }

    private val warningCancelDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),getString(R.string.cancel),getString(R.string.confirm_cancel_booking)){
            cancelBooking()
        }
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

        (requireActivity() as MainActivity).hideBottomNav()


        binding.loadingLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }

        bookingAdapter = BookingListItemAdapter(
            {
                findNavController().navigate(BookingFragmentDirections.actionBookingFragmentToBookingDetailSpaFragment(it, "history"))
            },
            {
                currentBooking = it
                warningCancelDialog.show()
            }
        )

        informationViewModel.user.observe(viewLifecycleOwner){
            bookingAdapter.differ.submitList(listOf())
            bookingAdapter.notifyDataSetChanged()
            serviceViewModel.getBookingHistory(it.id)
        }

        serviceViewModel.bookingList.observe(viewLifecycleOwner){
            bookingAdapter.differ.submitList(it)
            bookingAdapter.notifyDataSetChanged()
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

    private fun cancelBooking() {
        loadingDialog.show()

        serviceViewModel.cancelBooking(currentBooking)
        bookingAdapter.differ.submitList(listOf())
        bookingAdapter.notifyDataSetChanged()
        binding.rvBookingList.adapter = null

        serviceViewModel.response.observe(viewLifecycleOwner) {response ->
            response?.let {
                response.body?.let {
                    serviceViewModel.response.value = null
                    loadingDialog.dismiss()
                    binding.rvBookingList.adapter = bookingAdapter
                    binding.loadingLayout.visibility = View.VISIBLE
                    serviceViewModel.getBookingHistory(informationViewModel.getUserID())
                }
            }

        }
    }
}