package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentAddPetBinding
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory


class AddPetFragment : Fragment(R.layout.fragment_add_pet) {
    val DEBUG_TAG = "AddPetFragment"
    private var _binding : FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    private var isEditing = false

    private val args : AddPetFragmentArgs by navArgs()

    private lateinit var currentPet : PetInfo
    private  var currentIndex : Int = -1
    private val carouselViewModel : InformationViewModel by activityViewModels {
        PetIntoViewModelFactory(InformationRepository())
    }

    private var newPet : PetInfo = PetInfo()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Glide.with(binding.root)
                .load(uri)
                .placeholder(R.drawable.avatar_1)
                .into(binding.imgAvatar)
            newPet.image = uri.toString()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")

        if(args.updatePet.name!=""){
            isEditing = true
            currentPet= args.updatePet
            currentIndex = args.indexCurrentPet
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
            imgAvatar.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btnAdd.setOnClickListener {
                val newPet = createPet()

                if(isEditing){
                    carouselViewModel.updatePet(newPet,currentIndex)
                }
                else{

                    carouselViewModel.addPet(newPet)
                }

                findNavController().popBackStack()
            }

        }

        if(isEditing){
            binding.apply {
                titleOwnerPet.text = "Update Pet"
                btnAdd.text = "Update"
                edtNamePet.setText(currentPet.name)
                edtAge.setText(currentPet.age.toString())
                edtColor.setText(currentPet.color)
                edtType.setText(currentPet.type)
                edtVaccine.setText(currentPet.vaccine.toString())
                edtWeight.setText(currentPet.weight.toString())
                edtVariety.setText(currentPet.variety.toString())
                Glide.with(binding.root)
                    .load(currentPet.image)
                    .placeholder(R.drawable.avatar_1)
                    .into(imgAvatar)
                when (currentPet.gender) {
                    "Male" -> {
                        rdGroupGender.check(R.id.rbMale)
                    }
                    "Female" -> {
                        rdGroupGender.check(R.id.rbFemale)
                    }
                    else -> {
                        rdGroupGender.check(R.id.rbOther)
                    }
                }
            }
        }
    }

    private fun createPet() : PetInfo{
        //TODO: Kiem tra tinh hop le
        newPet.name = binding.edtNamePet.text.toString().trim()
        newPet.type = binding.edtType.text.toString().trim()
        newPet.age = binding.edtAge.text.toString().trim().toInt()
        newPet.variety = binding.edtVariety.text.toString().trim()
        newPet.weight = binding.edtWeight.text.toString().trim()
        newPet.color = binding.edtColor.text.toString().trim()
        newPet.gender = if(binding.rbMale.isSelected){
            "Male"
        } else if (binding.rbFemale.isSelected){
            "Female"
        } else{
            "Other"
        }
        newPet.vaccine = binding.edtVaccine.text.toString().trim().toInt()
        if(isEditing){
            newPet.apply {
                id = currentPet.id
            }
        }

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