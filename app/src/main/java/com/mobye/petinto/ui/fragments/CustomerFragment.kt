package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentCustomerBinding
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class CustomerFragment : Fragment(R.layout.fragment_customer) {

    private var _binding : FragmentCustomerBinding? = null
    private val binding get() = _binding!!
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            fillField(it!!)
        }

        binding.apply {
            btnSave.setOnClickListener {
                if(validate()){
                    saveInfo()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etName.text.isBlank()){
            binding.etName.error = "Please provide a name"
            isValidated  = false
        }else{
            binding.etName.error = null
        }
        if(binding.etPhone.text.isBlank()){
            binding.etPhone.error = "Please provide a phone number"
            isValidated  = false
        }else{
            binding.etPhone.error = null
        }
        return isValidated
    }

    private fun saveInfo() {
        val name = binding.etName.text.trim().toString()
        val phone = binding.etPhone.text.trim().toString()

        Log.e("CustomerPickup",phone)

        informationViewModel.updateCustomerPickup(
            CustomerPickup("", name,phone)
        )
    }

    private fun fillField(customerPickup: CustomerPickup) {
        binding.apply {
            etName.setText(customerPickup.name)
            if(customerPickup.phone.isNotBlank()) etPhone.setText(customerPickup.phone)
        }
    }

}