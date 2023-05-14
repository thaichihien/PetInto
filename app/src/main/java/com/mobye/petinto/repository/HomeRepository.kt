package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.database.NotificationDatabase
import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.models.Notification
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
    suspend fun getAllNotification() = NotificationDatabase.getAllNotification()

    suspend fun removeNotification(notification: Notification) =  NotificationDatabase.remove(notification)

    suspend fun clearNotification() = NotificationDatabase.removeAll()
}