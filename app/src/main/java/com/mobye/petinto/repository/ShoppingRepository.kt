package com.mobye.petinto.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petinto.R
import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.database.CartItemDatabase
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.Product
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.PetOrder
import com.mobye.petinto.models.apimodel.ProductOrder
import retrofit2.Response
import retrofit2.http.Query

class ShoppingRepository : IRepository {


    fun getProductSource(
        query : String,
        min : String,
        max : String,
        type : String
    ) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {ProductPagingSource(query,min,max,type)})
        .flow

    fun getPetSource(query : String,
        type: String,
        gender : String,
        minAge : String,
        maxAge : String,
        minPrice : String,
        maxPrice : String
    ) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {PetPagingSource(query,type,gender, minPrice, maxPrice, minAge, maxAge)})
        .flow


    suspend fun getAllCartItems() : List<CartItem> = CartItemDatabase.getAllCart()

    suspend fun saveCartItem(cartItem: CartItem) {
        CartItemDatabase.createOrUpdate(cartItem)
    }

    suspend fun deleteCartItem(cartItem: CartItem){
        CartItemDatabase.remove(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem){
        CartItemDatabase.update(cartItem)
    }

    suspend fun clearCart(){
        CartItemDatabase.removeAll()
    }

    suspend fun sendProductOrder(order : ProductOrder) : Response<ApiResponse<Any>>
        = RetrofitInstance.api.sendProductOrder(order)

    suspend fun sendPetOrder(order : PetOrder) : Response<ApiResponse<Any>>
            = RetrofitInstance.api.sendPetOrder(order)
}