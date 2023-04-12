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
import com.mobye.petinto.adapters.DeliveryInfoAdapter
import com.mobye.petinto.databinding.FragmentDeliveryAddressBinding
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory


class DeliveryAddressFragment : Fragment(R.layout.fragment_delivery_address) {

    private var _binding : FragmentDeliveryAddressBinding? = null
    private val binding get() = _binding!!
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }
    private lateinit var deliveryInfoAdapter: DeliveryInfoAdapter
    private var selectedIndex = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeliveryAddressBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deliveryInfoAdapter = DeliveryInfoAdapter (
            {
                selectedIndex = it
                deliveryInfoAdapter.notifyDataSetChanged()
                //informationViewModel.setDefaultDeliveryAddress(selectedIndex,true)
            },
            {
                //informationViewModel.setDefaultDeliveryAddress(it,false)
            },
            {
                findNavController().navigate(DeliveryAddressFragmentDirections.actionDeliveryAddressFragmentToDetailsDeliveryAddressFragment(
                    it
                ))
            }
        )

        informationViewModel.getAllDeliveryAddress(informationViewModel.getUserID())
        informationViewModel.deliveryList.observe(viewLifecycleOwner){
            deliveryInfoAdapter.differ.submitList(it)
        }

        binding.apply {
            rvDeliveryAddress.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = deliveryInfoAdapter
            }
            btnBack.setOnClickListener {
                saveDefaultAddress()
            }
            btnSaveDefaultAddress.setOnClickListener {
                saveDefaultAddress()
            }
            btnAddAddress.setOnClickListener {
                findNavController().navigate(DeliveryAddressFragmentDirections.actionDeliveryAddressFragmentToDetailsDeliveryAddressFragment(
                    DeliveryInfo()
                ))
            }
        }
    }


    private fun saveDefaultAddress() {
        if(selectedIndex >= 0){
            informationViewModel.setDefaultDeliveryAddress(selectedIndex)
        }
        findNavController().popBackStack()
    }

}