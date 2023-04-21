package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.apimodel.Booking

class ServiceRepository : IRepository {

    suspend fun sendSpaBooking(spaBooking: Booking)
        = RetrofitInstance.api.sendSpaBooking(spaBooking)

}