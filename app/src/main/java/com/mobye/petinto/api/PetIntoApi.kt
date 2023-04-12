package com.mobye.petinto.api

import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.Product
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.Order
import com.mobye.petinto.models.apimodel.ProductOrder
import com.mobye.petinto.models.apimodel.SpaBooking
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

    @GET("/data/news")
    suspend fun getNews(
    ) : Response<ApiResponse<List<Advertisement>>>

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

    //Booking
    @POST("/booking/spa/new")
    suspend fun sendSpaBooking(
        @Body booking: SpaBooking
    ) : Response<ApiResponse<Any>>


    //Pets
    @GET("/data/pets")
    suspend fun getPets(
        @Query("page") page: Int
    ) : Response<ApiResponse<List<PetInfo>>>
}