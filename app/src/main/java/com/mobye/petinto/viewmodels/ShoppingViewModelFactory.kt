package com.mobye.petinto.viewmodels

import android.icu.text.IDNA.Info
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petinto.repository.HomeRepository
import com.mobye.petinto.repository.IRepository
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository

@Suppress("UNCHECKED_CAST")
class PetIntoViewModelFactory(
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            ShoppingViewModel::class.java ->ShoppingViewModel(repository as ShoppingRepository) as T
            InformationViewModel::class.java -> InformationViewModel(repository as InformationRepository) as T
            HomeViewModel::class.java -> HomeViewModel(repository as HomeRepository) as T
            else -> throw IllegalArgumentException("Unknown View Model")
        }
    }
}