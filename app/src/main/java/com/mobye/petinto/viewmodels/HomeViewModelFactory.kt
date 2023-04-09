package com.mobye.petinto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petinto.repository.HomeRepository

class HomeViewModelFactory(private val repository: HomeRepository)
    : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InformationViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}