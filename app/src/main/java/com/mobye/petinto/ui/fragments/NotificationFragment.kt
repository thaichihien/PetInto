package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.adapters.NotificationItemAdapter
import com.mobye.petinto.databinding.FragmentNotificationBinding
import com.mobye.petinto.repository.HomeRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.HomeViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class NotificationFragment : Fragment() {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by activityViewModels {
        PetIntoViewModelFactory(HomeRepository())
    }

    private lateinit var notificationAdapter : NotificationItemAdapter

    private val warningDeleteDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),
        "Clear all notification",
        "Do you really want to clear all notification?"){
            clearNotification()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).hideBottomNav()

        notificationAdapter = NotificationItemAdapter(
            {i ->
                homeViewModel.removeNotification(i)
            },
            {
                handleNotification(it)
            }
        )

        homeViewModel.getNotification()
        homeViewModel.notificationList.observe(viewLifecycleOwner){
            notificationAdapter.differ.submitList(it)
        }

        binding.apply {

            val notiLayoutManager = LinearLayoutManager(requireContext())
            notiLayoutManager.reverseLayout = true
            notiLayoutManager.stackFromEnd = true
            rvNotification.apply {
                layoutManager = notiLayoutManager
                adapter = notificationAdapter
            }
            btnClearAll.setOnClickListener {
                warningDeleteDialog.show()
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun handleNotification(type: String) {
        when(type){
           "BOOKING" -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToBookingFragment())
           }
            "ORDER" -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToHistoryInformation())
            }
            "PET" ->{
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToHistoryPetFragment())
            }
        }
    }

    private fun clearNotification(){
        homeViewModel.clearAllNotification()
    }
}