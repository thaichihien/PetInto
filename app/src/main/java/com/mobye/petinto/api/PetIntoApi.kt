package com.mobye.petinto.api

import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.Product
import com.mobye.petinto.models.apimodel.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PetIntoApi {

    @GET("/data/products")
    suspend fun getProducts(
        @Query("page") page: Int
    ) : Response<ApiResponse<List<Product>>>

    @POST("/user/add")
    suspend fun sendUser(
        @Body user : Customer
    ) : Response<ApiResponse<Any>>

    @GET("/user/info")
    suspend fun getUser(
        @Query("id") id : String
    ) : Response<ApiResponse<Customer>>

}