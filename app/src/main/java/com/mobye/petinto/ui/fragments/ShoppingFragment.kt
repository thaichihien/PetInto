package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.ProductItemAdapter
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val TAG = "ShoppingFragment"
    private var _binding : FragmentShoppingBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }

    private lateinit var productAdapter : ProductItemAdapter

    private var firstTimeLoad = true

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
        shoppingViewModel.getCartItems()

        if(shoppingViewModel.lostNetwork && (requireActivity() as MainActivity).hasInternetConnection()){
            productAdapter.retry()
            shoppingViewModel.lostNetwork = false
        }

        lifecycleScope.launchWhenCreated {
            shoppingViewModel.productItemList.collectLatest {
                productAdapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }

        lifecycleScope.launchWhenCreated {
            productAdapter.loadStateFlow.collect{loadState ->
                binding.loadingBar.isVisible = loadState.source.append is LoadState.Loading
            }
        }

        productAdapter.addLoadStateListener {loadState ->
            if(loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading){

                if(productAdapter.itemCount == 0){
                    binding.loadingLayout.apply {
                        isVisible =true
                        startShimmer()
                    }
                }

            }else{
                if(firstTimeLoad){
                    if(!(requireActivity() as MainActivity).hasInternetConnection()){
                        // show error at empty fragment
                    }
                    binding.loadingLayout.isVisible = false
                    firstTimeLoad = false
                }
                binding.loadingLayout.isVisible = false

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    // TODO show net work error
                    shoppingViewModel.lostNetwork = true
                    Toast.makeText(requireContext(),"No Internet Connection",Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.apply {
            rvShoppingItem.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = productAdapter
            }
            btnCartShopping.setOnClickListener {
                findNavController().navigate(ShoppingFragmentDirections.shoppingFragmentToCartFragment())
            }
            btnFilter.setOnClickListener {
                findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToProductFilterFragment())
            }

            refreshLayout.setOnRefreshListener {
                if((requireActivity() as MainActivity).hasInternetConnection()){
                    productAdapter.retry()
                }else{}
                binding.refreshLayout.isRefreshing = false
            }

            etSearchProduct.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    lifecycleScope.launch{
                        productAdapter.submitData(PagingData.empty())
                        productAdapter.notifyDataSetChanged()
                        shoppingViewModel.searchProduct(binding.etSearchProduct.text.toString().trim())
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
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