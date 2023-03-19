package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager

import com.mobye.petinto.R
import com.mobye.petinto.adapters.ShoppingItemAdapter
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.models.ShoppingItem


class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    private var _binding : FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    private lateinit var testList : MutableList<ShoppingItem>
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testList = mutableListOf(
            ShoppingItem(
                name = "Bed",
                price = 345000,
                type = "Cat",
                stock = 2,
                detail = "32x32x28cm",
                image = R.drawable.home
            ),
            ShoppingItem(
                name = "Backpack",
                price = 600000,
                type = "All",
                stock = 12,
                detail = "42x31x28cm",
                image = R.drawable.balo
            ),
            ShoppingItem(
                name = "Food",
                price = 199000,
                type = "Dog",
                stock = 3,
                detail = "3kg",
                image = R.drawable.food_dog
            ),
            ShoppingItem(
                name = "Cage",
                price = 275000,
                type = "Mouse",
                stock = 3,
                detail = "27x21x30cm",
                image = R.drawable.house
            )
        )

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingItemAdapter = ShoppingItemAdapter()
        shoppingItemAdapter.differ.submitList(testList)

        binding.apply {
            rvShoppingItem.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = shoppingItemAdapter
            }
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}