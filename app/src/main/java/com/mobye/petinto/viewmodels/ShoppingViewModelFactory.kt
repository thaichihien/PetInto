package com.mobye.petinto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petinto.repository.ShoppingRepository

@Suppress("UNCHECKED_CAST")
class ShoppingViewModelFactory(
    private val repository: ShoppingRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingViewModel::class.java)){
            return ShoppingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}