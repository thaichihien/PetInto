package com.mobye.petinto.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobye.petinto.R
import com.mobye.petinto.adapters.CartItemAdapter
import com.mobye.petinto.databinding.FragmentCartBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.helpers.SwipeHelper
import com.mobye.petinto.models.ShoppingItem
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory


class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory(ShoppingRepository())
    }

    private lateinit var cartItemAdapter : CartItemAdapter
    private var isSelectedAll = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.hideBottomNav()
        cartItemAdapter = CartItemAdapter (
            { cartItem, i ->
            shoppingViewModel.removeFromCart(i)
            },
            {
                shoppingViewModel.changeQuantity(it,1)
            },
            {
                shoppingViewModel.changeQuantity(it,-1)
            },
            { isSelected,index ->
                shoppingViewModel.changeTotal(index,isSelected)



                if(binding.cbSelectAll.isChecked && !isSelected){
                    binding.cbSelectAll.isChecked = false
                    isSelectedAll = false
                }else if(shoppingViewModel.isSelectedAll()){
                    binding.cbSelectAll.isChecked = true
                    isSelectedAll = true
                }
            },
            { index,amount ->
                shoppingViewModel.changeTotal(index,amount)
            }
        )
        //cartItemAdapter.differ.submitList(shoppingViewModel.cartItemList.value)
        shoppingViewModel.cartItemList.observe(viewLifecycleOwner) {
            cartItemAdapter.differ.submitList(it)
        }
        shoppingViewModel.total.observe(viewLifecycleOwner) {
            binding.tvTotalCart.text = "%,d đ".format(it)
        }


        binding.apply {
            rvCartItem.apply {
                adapter = cartItemAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnBackCart.setOnClickListener {
                findNavController().popBackStack()
            }

            isSelectedAll = shoppingViewModel.isSelectedAll()
            cbSelectAll.isChecked = isSelectedAll

            cbSelectAll.setOnClickListener {
                isSelectedAll = !isSelectedAll
                shoppingViewModel.selectAllCart(isSelectedAll)
                if(!isSelectedAll) shoppingViewModel.resetTotal()
                cartItemAdapter.notifyDataSetChanged()
            }
//            cbSelectAll.setOnCheckedChangeListener { _, isChecked ->
//
//            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}