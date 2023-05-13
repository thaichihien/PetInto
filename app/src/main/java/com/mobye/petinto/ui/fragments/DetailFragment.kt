package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel


class DetailFragment () : Fragment(R.layout.fragment_detail) {

    val DEBUG_TAG = "DetailFragment"
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }

    private val args : DetailFragmentArgs by navArgs()

    private var quantity : Int = 1
    private var stock : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()
        val item = args.item

        Glide.with(view)
            .load(item.image)
            .into(binding.ivItemDetail)

        stock = item.stock

        binding.apply {
            tvItemNameDetails.text = item.name
            tvItemPriceDetails.text = Utils.formatMoneyVND(item.price)
            tvItemTypeDetails.text = item.typePet
            tvItemDetailDetails.text = item.detail
            tvItemStockDetails.text = item.stock.toString()
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            btnCart.setOnClickListener {
                findNavController().navigate(DetailFragmentDirections.detailFragmentToCartFragment())
            }
            btnIncrease.setOnClickListener {
                addQuantity(1)
            }
            btnDecrease.setOnClickListener {
                addQuantity(-1)
            }
            btnReport.setOnClickListener {
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToReportFragment())
            }
            btnBuy.setOnClickListener {
                addToCart()
            }




        }

    }

    private fun addToCart() {
        val item = args.item

        stock -= quantity
        binding.tvItemStockDetails.text = stock.toString()

        Toast.makeText(requireContext(),"${item.name} is added to your cart", Toast.LENGTH_SHORT).show()
        shoppingViewModel.addToCart(item,quantity)
    }

    private fun addQuantity(amount: Int) {
        quantity += amount

        if(quantity <= 0){
            quantity = 0
        }else if(quantity > stock){
            quantity = stock
        }

        binding.tvQuantity.text = quantity.toString()
    }


}