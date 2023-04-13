package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.SpaBooking
import com.mobye.petinto.repository.ServiceRepository
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val TAG = "ServiceViewModel"
    val response : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }

    fun sendSpaBooking(spaBooking: SpaBooking){
        viewModelScope.launch {
            try {
                val response = repository.sendSpaBooking(spaBooking)
                this@ServiceViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }



}