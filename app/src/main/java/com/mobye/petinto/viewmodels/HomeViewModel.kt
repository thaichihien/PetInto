package com.mobye.petinto.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _newsList = MutableSharedFlow<List<Advertisement>>(replay = 0)
    val newsList : SharedFlow<List<Advertisement>>
        get() = _newsList

    fun getNews(){
        viewModelScope.launch {
            repository.getNews().collect{
                _newsList.emit(it)
            }
        }
    }


}