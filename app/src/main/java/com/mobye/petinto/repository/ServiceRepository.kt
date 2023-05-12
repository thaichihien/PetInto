package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.apimodel.Booking

class ServiceRepository : IRepository {

    suspend fun sendBooking(spaBooking: Booking)
        = RetrofitInstance.api.sendBooking(spaBooking)

    suspend fun sendPaymentBooking(booking: Booking)
            = RetrofitInstance.api.sendPaymentBooking(booking)

    suspend fun destroyBooking(booking: Booking)
            = RetrofitInstance.api.destroyBooking(booking)
    suspend fun cancelBooking(booking: Booking)
            = RetrofitInstance.api.cancelBooking(booking)
    suspend fun getBookingHistory(id : String)
        = RetrofitInstance.api.getBookingHistory(id)

}