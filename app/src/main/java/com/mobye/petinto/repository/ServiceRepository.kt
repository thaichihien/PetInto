package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.apimodel.Booking

class ServiceRepository : IRepository {

    suspend fun sendBooking(spaBooking: Booking)
        = RetrofitInstance.api.sendBooking(spaBooking)

    suspend fun getBookingHistory(id : String)
        = RetrofitInstance.api.getBookingHistory(id)

}