package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.apimodel.SpaBooking

class ServiceRepository : IRepository {

    suspend fun sendSpaBooking(spaBooking: SpaBooking)
        = RetrofitInstance.api.sendSpaBooking(spaBooking)

}