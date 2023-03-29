package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.ShoppingItem
import com.mobye.petinto.repository.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shopItemList :MutableLiveData<List<ShoppingItem>> by lazy { MutableLiveData(listOf()) }
    val cartItemList : MutableLiveData<List<CartItem>> by lazy { MutableLiveData(listOf()) }
    val total : MutableLiveData<Int> by lazy { MutableLiveData(0) }

    val shopOrderList :MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }

    fun getOrderList(){
        viewModelScope.launch {
            shopOrderList.value = repository.getPetItems()
        }
    }

    fun getShoppingItems(){
        viewModelScope.launch {
            shopItemList.value = repository.getShoppingItems()
        }
    }

    fun getCartItems(){
        viewModelScope.launch {
            cartItemList.value = repository.getAllCartItems()
        }
    }

    fun addToCart(item : ShoppingItem,quantity : Int){
        val cartList = cartItemList.value!!.toMutableList()

        if(!addQuantity(item,cartList,quantity)){
            val newCartItem = CartItem(item,quantity)
            cartList.add(newCartItem)
            viewModelScope.launch{
                repository.saveCartItem(newCartItem)
            }
        }

        cartItemList.value = cartList
    }

    private fun addQuantity(item: ShoppingItem,list: MutableList<CartItem>,quantity: Int) : Boolean{
        for((index,cartItem) in list.withIndex()){
            if(cartItem.item!!.id == item.id){
                cartItem.quantity += quantity
                list[index] = cartItem
                viewModelScope.launch{
                    repository.saveCartItem(cartItem)
                }

                return true
            }
        }
        return false
    }

    fun removeFromCart(index : Int){
        val cartList = cartItemList.value!!.toMutableList()

        cartList.removeAt(index)
        viewModelScope.launch{
            repository.deleteCartItem(cartList[index])
        }


        cartItemList.value = cartList
    }

    fun changeQuantity(index :Int,number: Int){
        val cartList = cartItemList.value!!.toMutableList()

        if(cartList[index].quantity >= 1){
            cartList[index].quantity += number
            viewModelScope.launch {
                repository.updateCartItem(cartList[index])
            }
        }
        // TODO deal with stock


        cartItemList.value = cartList

    }

    fun changeTotal(index : Int,isAdded: Boolean){
        val cartList = cartItemList.value
        val cartItem = cartList!![index]
        cartItem.selected = isAdded

        if (isAdded){
            total.value = total.value?.plus((cartItem.item!!.price * cartItem.quantity))
        }else{
            total.value = total.value?.minus((cartItem.item!!.price * cartItem.quantity))
        }
    }

    fun changeTotal(index: Int,number : Int){
        val cartList = cartItemList.value
        val cartItem = cartList!![index]

        total.value = total.value?.plus(cartItem.item!!.price * number)
    }

    fun resetTotal(){
        total.value = 0
    }

    fun selectAllCart(yes : Boolean){
        val cartList = cartItemList.value!!.toMutableList()

        total.value = 0
        for(cartItem in cartList){
            cartItem.selected = yes
            total.value = total.value!! + (cartItem.item!!.price * cartItem.quantity)
        }


        cartItemList.value = cartList
    }

    fun isSelectedAll() : Boolean{
        val cartList = cartItemList.value!!.toMutableList()

        for(cartItem in cartList){
           if(!cartItem.selected) return false
        }

        return true
    }

    fun clearAllCart(){
        val cartList = cartItemList.value!!.toMutableList()
        cartList.clear()
        viewModelScope.launch{
            repository.clearCart()
        }
        cartItemList.value = cartList
    }






}