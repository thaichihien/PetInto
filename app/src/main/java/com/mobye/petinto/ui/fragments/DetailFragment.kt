package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailBinding
import com.mobye.petinto.models.ShoppingItem
import com.mobye.petinto.ui.MainActivity


class DetailFragment () : Fragment(R.layout.fragment_detail) {

    val DEBUG_TAG = "DetailFragment"
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args : DetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()
        val item = args.itemDetail

        Glide.with(view)
            .load(item.image)
            .into(binding.ivItemDetail)

        binding.tvItemNameDetails.text = item.name
        binding.tvItemPriceDetails.text = item.price.toString()
        binding.tvItemTypeDetails.text = item.type
        binding.tvItemDetailDetails.text = item.detail
        binding.tvItemStockDetails.text = item.stock.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.e(DEBUG_TAG,"onDestroyView")
    }


}