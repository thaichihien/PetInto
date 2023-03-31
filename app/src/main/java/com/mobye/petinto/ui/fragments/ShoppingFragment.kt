package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager

import com.mobye.petinto.R
import com.mobye.petinto.adapters.ProductItemAdapter
import com.mobye.petinto.adapters.ShoppingItemAdapter
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.models.Product
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val DEBUG_TAG = "ShoppingFragment"
    private var _binding : FragmentShoppingBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory(ShoppingRepository())
    }

    private lateinit var testList : MutableList<Product>
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter
    private lateinit var productAdapter : ProductItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")
//        testList = mutableListOf(
//            ShoppingItem(
//                name = "Bed",
//                price = 345000,
//                type = "Cat",
//                stock = 2,
//                detail = "32x32x28cm",
//                image = R.drawable.home
//            ),
//            ShoppingItem(
//                name = "Backpack",
//                price = 600000,
//                type = "All",
//                stock = 12,
//                detail = "42x31x28cm",
//                image = R.drawable.balo
//            ),
//            ShoppingItem(
//                name = "Food",
//                price = 199000,
//                type = "Dog",
//                stock = 3,
//                detail = "3kg",
//                image = R.drawable.food_dog
//            ),
//            ShoppingItem(
//                name = "Cage",
//                price = 275000,
//                type = "Mouse",
//                stock = 3,
//                detail = "27x21x30cm",
//                image = R.drawable.house
//            )
//        )

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingItemAdapter = ShoppingItemAdapter(
            {
                val action = ShoppingFragmentDirections.navigateToDetailFragment(it)
                findNavController().navigate(action)
            },
            { item,quantity ->
                Toast.makeText(requireContext(),"${item.name} is added to your cart",Toast.LENGTH_SHORT).show()
                shoppingViewModel.addToCart(item,quantity)
            }
        )

        productAdapter = ProductItemAdapter(
            {
                val action = ShoppingFragmentDirections.navigateToDetailFragment(it)
                findNavController().navigate(action)
            },
            { item,quantity ->
                Toast.makeText(requireContext(),"${item.name} is added to your cart",Toast.LENGTH_SHORT).show()
                shoppingViewModel.addToCart(item,quantity)
            }
        )

        //shoppingViewModel.getShoppingItems()
        shoppingViewModel.getCartItems()
//        shoppingViewModel.shopItemList.observe(viewLifecycleOwner) {
//            shoppingItemAdapter.differ.submitList(it)
//        }


        lifecycleScope.launchWhenCreated {
            shoppingViewModel.productItemList.collectLatest {
                productAdapter.submitData(it)

            }
        }

        lifecycleScope.launchWhenCreated {
            productAdapter.loadStateFlow.collect{
                binding.loadingBar.isVisible = it.source.append is LoadState.Loading
            }
        }


        //shoppingItemAdapter.differ.submitList(testList)

        binding.apply {
            rvShoppingItem.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = productAdapter
            }
            btnCartShopping.setOnClickListener {
                findNavController().navigate(ShoppingFragmentDirections.shoppingFragmentToCartFragment())
            }
        }

    }


    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}