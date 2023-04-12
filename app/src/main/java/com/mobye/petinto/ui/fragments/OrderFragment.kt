package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.OrderAdapter
import com.mobye.petinto.databinding.FragmentOrderBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory
import kotlinx.coroutines.flow.collectLatest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment(R.layout.fragment_order) {

    val DEBUG_TAG = "OrderFragment"
    private var _binding : FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel : ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory(ShoppingRepository())
    }

    private var firstTimeLoad = true

    private lateinit var orderItemAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderItemAdapter = OrderAdapter ({
            val action = OrderFragmentDirections.actionOrderFragmentToPetPaymentFragment(it)
            findNavController().navigate(action)
        },{

            var action = OrderFragmentDirections.actionOrderFragmentToDetailPetFragment(it)
            findNavController().navigate(action)
        })

        if(orderViewModel.lostNetwork && (requireActivity() as MainActivity).hasInternetConnection()){
            Log.e("RETRY_ORDER_PET","yes")
            orderItemAdapter.retry()
            orderViewModel.lostNetwork = false
        }

        lifecycleScope.launchWhenCreated {
            orderViewModel.petItemList.collectLatest {
                orderItemAdapter.submitData(it)
            }
        }

        orderItemAdapter.addLoadStateListener {loadState ->
            if(loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading){
                Log.e(DEBUG_TAG,"firstTimeLoad : $firstTimeLoad")
                if(firstTimeLoad){
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

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    // TODO show net work error
                    orderViewModel.lostNetwork = true
                    Toast.makeText(requireContext(),"error : ${errorState.error}", Toast.LENGTH_SHORT).show()

                }
            }
        }


        binding.apply {
            rvOrderItem.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = orderItemAdapter
            }
        }
    }

}