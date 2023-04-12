package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.models.Advertisement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepository : IRepository{

    suspend fun getNews() : Flow<List<Advertisement>> = flow {
        val response = RetrofitInstance.api.getNews()
        if(response.isSuccessful){
            emit(response.body()!!.body!!)
        }else{
            //TODO server error
        }
    }


}