package com.mobye.petinto.api

import com.mobye.petinto.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PetIntoApi {

    @GET("/data/products")
    suspend fun getProducts(
        @Query("page") page: Int
    ) : Response<List<Product>>


}