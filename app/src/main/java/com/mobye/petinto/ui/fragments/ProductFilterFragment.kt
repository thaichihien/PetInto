package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.databinding.FragmentProductFilterBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Constants
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel

class ProductFilterFragment : Fragment() {
    private var _binding : FragmentProductFilterBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductFilterBinding.inflate(layoutInflater)
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
        binding.apply {
            spType.adapter = typeListAdapter

            btnClearAll.setOnClickListener {
                shoppingViewModel.clearFilter()
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

            true
        }catch (e : Exception){
            binding.apply {
                etMin.error = "Please enter an integer value"
                etMax.error = "Please enter an integer value"
            }
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
            shoppingViewModel.applyFilter(
                etMin.text.toString().trim(),
                etMax.text.toString().trim(),
                type
            )
        }
    }

    private fun clearFilter() {
        binding.apply {
            spType.setSelection(0)
            etMin.setText("")
            etMax.setText("")
        }
    }

    private fun fillFilter() {
        binding.apply {
            spType.setSelection(Utils.getProductTypeIndex(shoppingViewModel.type))
            etMin.setText(shoppingViewModel.min)
            etMax.setText(shoppingViewModel.max)
        }
    }
}