package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.CartItemAdapter
import com.mobye.petinto.databinding.FragmentCartBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
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
        cartItemAdapter = CartItemAdapter()
        //cartItemAdapter.differ.submitList(shoppingViewModel.cartItemList.value)
        shoppingViewModel.cartItemList.observe(viewLifecycleOwner) {
            cartItemAdapter.differ.submitList(it)
        }


        binding.apply {
            rvCartItem.apply {
                adapter = cartItemAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}