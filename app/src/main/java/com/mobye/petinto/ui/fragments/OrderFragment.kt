package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.OrderAdapter
import com.mobye.petinto.adapters.ShoppingItemAdapter
import com.mobye.petinto.databinding.FragmentOrderBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.models.ShoppingItem
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory

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
        orderItemAdapter = OrderAdapter {
            val action = OrderFragmentDirections.actionOrderFragmentToPetPaymentFragment(it)
            findNavController().navigate(action)
        }
        orderViewModel.getOrderList()
        orderViewModel.shopOrderList.observe(viewLifecycleOwner) {
            orderItemAdapter.differ.submitList(it)
        }

        binding.apply {
            rvOrderItem.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = orderItemAdapter
            }
        }
    }

}