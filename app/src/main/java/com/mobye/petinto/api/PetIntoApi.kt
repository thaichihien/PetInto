package com.mobye.petinto.api

import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.Product
import com.mobye.petinto.models.apimodel.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PetIntoApi {

    @GET("/data/products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("query") query : String = ""
    ) : Response<ApiResponse<List<Product>>>

    @GET("/data/news")
    suspend fun getNews(
    ) : Response<ApiResponse<List<Advertisement>>>

    @GET("/data/history")
    suspend fun getOrderHistory(
        @Query("id") id: String
    ) : Response<ApiResponse<List<OrderHistory>>>

    @POST("/user/add")
    suspend fun sendUser(
        @Body user : Customer,
        @Query("google") isGoogle: Boolean = false
    ) : Response<ApiResponse<Any>>


    @GET("/user/info")
    suspend fun getUser(
        @Query("id") id : String
    ) : Response<ApiResponse<Customer>>


    //Order
    @POST("/order/new")
    suspend fun sendProductOrder(
        @Body order: ProductOrder
    ) : Response<ApiResponse<Any>>


    @POST("/order/pet")
    suspend fun sendPetOrder(
        @Body order: PetOrder
    ) : Response<ApiResponse<Any>>



    //Booking
    @POST("/booking/new")
    suspend fun sendBooking(
        @Body booking: Booking
    ) : Response<ApiResponse<Booking>>


    //Pets
    @GET("/data/pets")
    suspend fun getPets(
        @Query("page") page: Int
    ) : Response<ApiResponse<List<PetInfo>>>
}