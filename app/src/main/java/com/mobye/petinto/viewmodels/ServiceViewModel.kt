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
import com.mobye.petinto.utils.Constants.Companion.NORMAL_ROOM_COST
import com.mobye.petinto.utils.Constants.Companion.VIP_ROOM_COST
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    private val TAG = "ServiceViewModel"
    val response : MutableLiveData<ApiResponse<Booking>> by lazy { MutableLiveData() }
    val bookingList : MutableLiveData<List<Booking>> by lazy { MutableLiveData(listOf())}

    val serviceList=listOf("Massage","Hair cut","Bath","Nail cut")
    val serviceCharge = listOf(300000,2300000,270000,100000)
    var checkIn : Date = Date()
    var checkOut = Date()

    val hotelCost : MutableLiveData<Int> by lazy { MutableLiveData(0) }


    fun getBookingHistory(id : String){
        viewModelScope.launch {
            try {
                val response = repository.getBookingHistory(id).body()!!
                if(response.result){
                    bookingList.value =  response.body
                }else{
                    Log.e("ServiceViewModel",response.error)
                }

            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }
    fun sendBooking(booking: Booking){
        viewModelScope.launch {
            try {
                val response = repository.sendBooking(booking)
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

    fun makePayment(booking: Booking,payment : String,note : String){

        booking.apply {
            this.payment = payment
            this.note = note
        }

        viewModelScope.launch {
            try {
                val response = repository.sendPaymentBooking(booking)
                this@ServiceViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }

    }

    fun destroyBooking(booking: Booking){
        viewModelScope.launch {
            try {
                val response = repository.destroyBooking(booking)
                this@ServiceViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }

    fun cancelBooking(booking: Booking){
        viewModelScope.launch {
            try {
                val response = repository.cancelBooking(booking)
                this@ServiceViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }



    fun calculateHotelCost(isVIP : Boolean){
        if(checkDateValid()){
            val millionSeconds = checkOut.time - checkIn.time
            val roomCost = if(isVIP) VIP_ROOM_COST else NORMAL_ROOM_COST
            val cost = TimeUnit.MILLISECONDS.toDays(millionSeconds) * roomCost
            hotelCost.value = cost.toInt()
        }
    }

    fun checkDateValid()
        = checkOut.time >= checkIn.time


}