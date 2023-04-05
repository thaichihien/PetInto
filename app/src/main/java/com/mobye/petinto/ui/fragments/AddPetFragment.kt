package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentAddPetBinding
import com.mobye.petinto.databinding.FragmentProfileBinding
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddPetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPetFragment : Fragment(R.layout.fragment_add_pet) {
    val DEBUG_TAG = "AddPetFragment"
    private var _binding : FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    private var isEditing = false

    private val args : AddPetFragmentArgs by navArgs()

    private lateinit var currentPet : PetInfo
    private val carouselViewModel : InformationViewModel by activityViewModels {
        InformationViewModelFactory(InformationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")

        if(args.updatePet.name!=""){
            isEditing = true
            currentPet= args.updatePet
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentAddPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(DEBUG_TAG,"onViewCreated")


        binding.apply {


            btnAdd.setOnClickListener {
                val newPet = createPet()

                if(isEditing){
                    carouselViewModel.updatePet(newPet, 0)
                }
                else{

                    carouselViewModel.addPet(newPet)
                }

                findNavController().popBackStack()
            }
        }

        if(isEditing){
            binding.apply {
                titleOwnerPet.setText("Update Pet")
                btnAdd.setText("Update")
                edtNamePet.setText(currentPet.name)
                edtAge.setText(currentPet.age.toString())
                edtColor.setText(currentPet.color)
                edtType.setText(currentPet.type)
                edtVaccine.setText(currentPet.vaccine.toString())
                edtWeight.setText(currentPet.weight.toString())
                if(currentPet.gender=="Male"){
                    rbMale.isSelected = true
                }
                else if(currentPet.gender=="Female"){
                    rbFemale.isSelected = true
                }
                else{

                }
            }
        }
    }

    private fun createPet() : PetInfo{
        //TODO: Kiem tra tinh hop le

        val name = binding.edtNamePet.text.toString().trim()
        val type = binding.edtType.text.toString().trim()
        val age = binding.edtAge.text.toString().trim().toInt()
        val variety = binding.edtVariety.text.toString().trim()
        val weight = binding.edtWeight.text.toString().trim().toDouble()
        val color = binding.edtColor.text.toString().trim()
        val gender = if(binding.rbMale.isSelected){
            "Male"
        } else if (binding.rbFemale.isSelected){
            "Female"
        } else{
            "Other"
        }
        val vaccine = binding.edtVaccine.text.toString().trim().toInt()

        val newPet = PetInfo("PET1111",
            name,
            0,
            type,
            "",
            R.drawable.cat,
            gender,
            age,
            vaccine,
            variety,
            weight,
            color
        )
        return newPet
    }

    override fun onPause() {
        super.onPause()
        Log.e(DEBUG_TAG,"onPause")
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