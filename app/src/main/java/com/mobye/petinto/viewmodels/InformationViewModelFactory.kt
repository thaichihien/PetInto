package com.mobye.petinto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petinto.repository.InformationRepository

class InformationViewModelFactory (private val repository: InformationRepository)
    :ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InformationViewModel::class.java)){
            return InformationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}