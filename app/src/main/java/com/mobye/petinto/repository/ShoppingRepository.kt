package com.mobye.petinto.repository

import com.mobye.petinto.R
import com.mobye.petinto.database.CartItemDatabase
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.ShoppingItem

class ShoppingRepository {



    suspend fun getShoppingItems() : List<ShoppingItem>
         = listOf(
            ShoppingItem(
                id = "1",
                name = "Bed",
                price = 345000,
                type = "Cat",
                stock = 2,
                detail = "32x32x28cm",
                image = R.drawable.home
            ),
            ShoppingItem(
                id = "2",
                name = "Backpack",
                price = 600000,
                type = "All",
                stock = 12,
                detail = "42x31x28cm",
                image = R.drawable.balo
            ),
            ShoppingItem(
                id = "3",
                name = "Food",
                price = 199000,
                type = "Dog",
                stock = 3,
                detail = "3kg",
                image = R.drawable.food_dog
            ),
            ShoppingItem(
                id = "4",
                name = "Cage",
                price = 275000,
                type = "Mouse",
                stock = 3,
                detail = "27x21x30cm",
                image = R.drawable.house
            )
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