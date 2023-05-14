package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.databinding.FragmentPetFilterBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Constants
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel

class PetFilterFragment : Fragment() {
    private var _binding : FragmentPetFilterBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).hideBottomNav()

        val typeListAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.productTypeList
        )

        val genderListAdapter = ArrayAdapter(
        requireActivity().baseContext,
        android.R.layout.simple_spinner_dropdown_item,
        Constants.genderList
        )

        binding.apply {
            spType.adapter = typeListAdapter

            spGender.adapter = genderListAdapter

            btnClearAll.setOnClickListener {
                shoppingViewModel.clearPetFilter()
                clearFilter()
            }
            btnExit.setOnClickListener {
                findNavController().popBackStack()
            }

            btnApply.setOnClickListener {
                if(validate()){
                    applyFilter()
                    findNavController().popBackStack()
                }
            }
        }
        fillFilter()
    }

    private fun validate(): Boolean {
        return try {
            with(binding){

                if(etMin.text.isNotBlank() && etMax.text.isNotBlank()){
                    val min = etMin.text.toString().toInt()
                    val max = etMax.text.toString().toInt()

                    if(min > max){
                        etMin.error = "Minimum price must be less than maximum price"
                        etMax.error = "Minimum price must be less than maximum price"
                        return false
                    }else{
                        etMin.error = null
                        etMax.error = null
                    }
                }

                if(etMinAge.text.isNotBlank() && etMaxAge.text.isNotBlank()){
                    val min = etMinAge.text.toString().toInt()
                    val max = etMaxAge.text.toString().toInt()

                    if(min > max){
                        etMinAge.error = "Minimum age must be less than maximum age"
                        etMaxAge.error = "Minimum age must be less than maximum age"
                        return false
                    }else{
                        etMinAge.error = null
                        etMaxAge.error = null
                    }
                }
                true
            }

        }catch (e : Exception){
            Toast.makeText(requireContext(),"Please enter an integer value",Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun applyFilter() {
        with(binding){
            val type = if(spType.selectedItemPosition == 0){
                ""
            }else{
                spType.selectedItem.toString()
            }
            val gender = if(spGender.selectedItemPosition == 0){
                ""
            }else{
                spGender.selectedItem.toString()
            }
            shoppingViewModel.applyPetFilter(
                type,gender,
                etMinAge.text.toString().trim(),
                etMaxAge.text.toString().trim(),
                etMin.text.toString().trim(),
                etMax.text.toString().trim()
            )
        }
    }

    private fun clearFilter() {
        binding.apply {
            spType.setSelection(0)
            spGender.setSelection(0)
            etMin.setText("")
            etMax.setText("")
            etMaxAge.setText("")
            etMinAge.setText("")
        }
    }
    private fun fillFilter() {
        binding.apply {
            spType.setSelection(Utils.getProductTypeIndex(shoppingViewModel.typePet))
            spGender.setSelection(Utils.getGenderIndex(shoppingViewModel.gender))
            etMin.setText(shoppingViewModel.minPrice)
            etMax.setText(shoppingViewModel.maxPrice)
            etMinAge.setText(shoppingViewModel.minAge)
            etMaxAge.setText(shoppingViewModel.maxAge)
        }
    }
}