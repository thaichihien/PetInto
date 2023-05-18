package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.OrderHistoryAdapter
import com.mobye.petinto.databinding.FragmentHistoryInformationBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class HistoryInformation : Fragment(R.layout.fragment_history_information) {

    val DEBUG_TAG = "HistoryFragment"
    private var _binding : FragmentHistoryInformationBinding? = null
    private val binding get() = _binding!!

    private val orderHistoryAdapter : OrderHistoryAdapter by lazy { OrderHistoryAdapter() }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val activity = activity as MainActivity
        activity.hideBottomNav()


        informationViewModel.user.observe(viewLifecycleOwner){
            it?.let {
                informationViewModel.getOrderHistory()
            }
        }
        informationViewModel.orderHistoryList.observe(viewLifecycleOwner){
            orderHistoryAdapter.differ.submitList(it)
        }

        binding.apply {
            rvOrderHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = orderHistoryAdapter
            }
            exitBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}