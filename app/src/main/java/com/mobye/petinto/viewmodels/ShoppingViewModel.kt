package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.ShoppingItem
import com.mobye.petinto.repository.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shopItemList :MutableLiveData<List<ShoppingItem>> by lazy { MutableLiveData(listOf()) }
    val cartItemList : MutableLiveData<List<CartItem>> by lazy { MutableLiveData(listOf()) }

    fun getShoppingItems(){
        viewModelScope.launch {
            shopItemList.value = repository.getShoppingItems()
        }
    }

    fun addToCart(item : ShoppingItem){
        val cartList = cartItemList.value!!.toMutableList()


        if(!addQuantity(item,cartList)){
            cartList.add(CartItem(item,1))
        }

        Log.e("SHOP_CART","add ${item.image} to cart : ${cartList.size}")
        cartItemList.value = cartList
    }

    private fun addQuantity(item: ShoppingItem,list: MutableList<CartItem>) : Boolean{
        for((index,cartItem) in list.withIndex()){
            if(cartItem.item.id == item.id){
                cartItem.quantity += 1
                list[index] = cartItem
                return true
            }
        }
        return false
    }





}