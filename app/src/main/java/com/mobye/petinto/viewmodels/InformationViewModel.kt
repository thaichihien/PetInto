package com.mobye.petinto.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository

class InformationViewModel(private val repository: InformationRepository) :
    ViewModel(){

    val myPetList : MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }

    fun addPet(pet : PetInfo){
        var list = myPetList.value!!.toMutableList()
        list.add(pet)
        myPetList.value = list
    }



}