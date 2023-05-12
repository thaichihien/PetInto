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

    fun getPetSource(query : String) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {PetPagingSource(query)})
        .flow
//    suspend fun getShoppingItems() : List<Product>
//         = listOf(
//            Product(
//                id = "1",
//                name = "Bed",
//                price = 345000,
//                typePet = "Cat",
//                stock = 2,
//                detail = "32x32x28cm",
//                image = R.drawable.home
//            ),
//            Product(
//                id = "2",
//                name = "Backpack",
//                price = 600000,
//                typePet = "All",
//                stock = 12,
//                detail = "42x31x28cm",
//                image = R.drawable.balo
//            ),
//            Product(
//                id = "3",
//                name = "Food",
//                price = 199000,
//                typePet = "Dog",
//                stock = 3,
//                detail = "3kg",
//                image = R.drawable.food_dog
//            ),
//            Product(
//                id = "4",
//                name = "Cage",
//                price = 275000,
//                typePet = "Mouse",
//                stock = 3,
//                detail = "27x21x30cm",
//                image = R.drawable.house
//            )
//        )

    suspend fun getPetItems() : List<PetInfo> = listOf(
        PetInfo("PET10001",
            "Golden Dog",
            11800000,
            "Dog",
            "",
            "Male",
            1,
            1,
            "Golden",
            "1.5",
            "Brown"),
        PetInfo("PET10002",
            "British Golden Cat",
            15000000,
            "Cat",
            "",
            "Female",
            2,
            2,
            "Golden",
            "1.2",
            "Yellow")
    )

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