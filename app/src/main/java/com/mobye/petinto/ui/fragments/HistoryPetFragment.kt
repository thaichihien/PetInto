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
import com.mobye.petinto.adapters.OrderHistoryAdapter
import com.mobye.petinto.adapters.OrderPetHistoryAdapter
import com.mobye.petinto.databinding.FragmentHistoryInformationBinding
import com.mobye.petinto.databinding.FragmentHistoryPetBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory


class HistoryPetFragment : Fragment(R.layout.fragment_history_pet) {

    val DEBUG_TAG = "HistoryPetFragment"
    private var _binding : FragmentHistoryPetBinding? = null
    private val binding get() = _binding!!

    private val orderPetHistoryAdapter : OrderPetHistoryAdapter by lazy { OrderPetHistoryAdapter() }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as MainActivity
        activity.hideBottomNav()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            exitBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        informationViewModel.user.observe(viewLifecycleOwner){
            it?.let {
                informationViewModel.getOrderHistory()
            }
        }
        informationViewModel.orderPetHistoryList.observe(viewLifecycleOwner){
            orderPetHistoryAdapter.differ.submitList(it)
        }

        binding.apply {
            rvOrderHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = orderPetHistoryAdapter
            }
        }


    }

}