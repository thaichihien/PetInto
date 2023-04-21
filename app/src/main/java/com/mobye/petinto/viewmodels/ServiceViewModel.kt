package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.repository.ServiceRepository
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val TAG = "ServiceViewModel"
    val response : MutableLiveData<ApiResponse<Booking>> by lazy { MutableLiveData() }


    val serviceList=listOf("Massage","Hair cut","Bath","Nail cut")
    val serviceCharge = listOf(300000,2300000,270000,100000)

    fun sendSpaBooking(spaBooking: Booking){
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

    fun createBooking(customerID : String,customerPickup: CustomerPickup ,pet : PetInfo, service : String,
    checkIn : String,checkOut : String,type : String,charge : Int) : Booking{
        val booking = Booking()

        booking.apply {
            this.customerID = customerID
            this.petName = pet.name
            this.genre = pet.type
            this.weight = pet.weight
            this.service = service
            this.checkIn = checkIn
            this.checkOut = checkOut
            this.type = type
            this.customerName = customerPickup.name
            this.phone = customerPickup.phone
            this.charge = charge
        }

        return booking
    }



}