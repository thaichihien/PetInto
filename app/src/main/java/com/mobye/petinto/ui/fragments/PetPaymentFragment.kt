package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailBinding
import com.mobye.petinto.databinding.FragmentPetPaymentBinding
import com.mobye.petinto.ui.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PetPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetPaymentFragment : Fragment(R.layout.fragment_pet_payment) {

    val DEBUG_TAG = "PetPaymentFragment"
    private var _binding : FragmentPetPaymentBinding? = null
    private val binding get() = _binding!!

    private val args : PetPaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()
        val item = args.petSelected

        Glide.with(view)
            .load(item.image)
            .into(binding.iconIV)

        binding.apply {
            tvFullname.text = item.name
            tvPrice.text = "%,d đ".format(item.price)
            tvColor.text = item.color
            tvDateBuy.text = "22/02/2023"
            btnBackPayment.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}