package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailPetBinding
import com.mobye.petinto.ui.MainActivity

class DetailPetFragment : Fragment(R.layout.fragment_detail_pet) {

    val DEBUG_TAG = "DetailPetFragment"
    private var _binding : FragmentDetailPetBinding? = null
    private val binding get() = _binding!!

    private val args : DetailPetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()
        val item = args.petItemDetail

        Glide.with(view)
            .load(item.image)
            .into(binding.ivItemDetail)

        binding.apply {
            tvItemNameDetailsPet.text = item.name
            tvItemPriceDetailsPet.text = "%,d đ".format(item.price)
            tvTypePet.text = item.type
            tvVaccinePet.text = item.vaccine.toString()
            tvVarietyPet.text = item.variety
            tvGenderPet.text = item.gender
            tvWeightPet.text = item.weight
            tvAgePet.text = item.age.toString()

             btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showBottomNav()
        Log.e(DEBUG_TAG,"onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.e(DEBUG_TAG,"onStart")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}