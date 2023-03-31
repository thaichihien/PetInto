package com.mobye.petinto.repository

import com.mobye.petinto.R
import com.mobye.petinto.database.CartItemDatabase
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.Product

class ShoppingRepository {

    fun getProductSource() = ProductPagingSource()
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
            "Available",
            R.drawable.dog,
            "Male",
            1,
            1,
            "Golden",
            1.5,
            "Brown"),
        PetInfo("PET10002",
            "British Golden Cat",
            15000000,
            "Cat",
            "Sold out",
            R.drawable.cat,
            "Female",
            2,
            2,
            "Golden",
            1.2,
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


}