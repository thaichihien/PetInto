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
        @Query("query") query : String = "",
        @Query("min") min : String = "",
        @Query("max") max : String = "",
        @Query("type") type : String = ""
    ) : Response<ApiResponse<List<Product>>>

    @GET("/data/news")
    suspend fun getNews(
    ) : Response<ApiResponse<List<Advertisement>>>

    @GET("/data/history")
    suspend fun getOrderHistory(
        @Query("id") id: String
    ) : Response<ApiResponse<List<OrderHistory>>>

    @GET("/data/pethistory")
    suspend fun getPetOrderHistory(
        @Query("id") id: String
    ) : Response<ApiResponse<List<PetOrderHistory>>>

    @POST("/user/add")
    suspend fun sendUser(
        @Body user : Customer,
        @Query("google") isGoogle: Boolean = false
    ) : Response<ApiResponse<Any>>

    @POST("/user/report")
    suspend fun sendReport(
        @Body report : Report
    ) : Response<ApiResponse<Any>>


    @POST("/user/info")
    suspend fun getUser(
        @Query("id") id : String,
        @Body token : Map<String, String>
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

    @POST("/booking/payment")
    suspend fun sendPaymentBooking(
        @Body booking: Booking
    ) : Response<ApiResponse<Booking>>

    @POST("/booking/delete")
    suspend fun destroyBooking(
        @Body booking: Booking
    ) : Response<ApiResponse<Booking>>

    @POST("/booking/cancel")
    suspend fun cancelBooking(
        @Body booking: Booking
    ) : Response<ApiResponse<Booking>>

    @GET("/booking/history")
    suspend fun getBookingHistory(
        @Query("id") id : String
    ) : Response<ApiResponse<List<Booking>>>


    //Pets
    @GET("/data/pets")
    suspend fun getPets(
        @Query("page") page: Int,
        @Query("query") query : String = ""
    ) : Response<ApiResponse<List<PetInfo>>>
}