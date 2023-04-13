package com.mobye.petinto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petinto.repository.*

@Suppress("UNCHECKED_CAST")
class PetIntoViewModelFactory(
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            ShoppingViewModel::class.java ->ShoppingViewModel(repository as ShoppingRepository) as T
            InformationViewModel::class.java -> InformationViewModel(repository as InformationRepository) as T
            HomeViewModel::class.java -> HomeViewModel(repository as HomeRepository) as T
            ServiceViewModel::class.java -> ServiceViewModel(repository as ServiceRepository) as T
            else -> throw IllegalArgumentException("Unknown View Model")
        }
    }
}