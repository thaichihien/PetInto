package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobye.petinto.models.*
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.CartOrder
import com.mobye.petinto.models.apimodel.Order
import com.mobye.petinto.models.apimodel.ProductOrder
import com.mobye.petinto.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
) : ViewModel() {

    private val TAG = "ShoppingViewModel"

    //ShoppingFragment
    var lostNetwork : Boolean = false
    val cartItemList : MutableLiveData<List<CartItem>> by lazy { MutableLiveData(listOf()) }
    val paymentItemList : MutableLiveData<List<CartItem>> by lazy { MutableLiveData(listOf()) }
    val total : MutableLiveData<Int> by lazy { MutableLiveData(0) }

    //PaymentFragment
    val response : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }


    val shopOrderList :MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }

    fun getOrderList(){
        viewModelScope.launch {
            shopOrderList.value = repository.getPetItems()
        }
    }

    fun getShoppingItems(){
        viewModelScope.launch {
            //shopItemList.value = repository.getShoppingItems()
        }
    }

    val productItemList : Flow<PagingData<Product>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {repository.getProductSource()})
        .flow
        .cachedIn(viewModelScope)



    fun getCartItems(){
        viewModelScope.launch {
            cartItemList.value = repository.getAllCartItems()
        }
    }

    fun getPaymentList(){
        viewModelScope.launch {
            cartListToPayment()
        }
    }

    private fun cartListToPayment(){
        val paymentList = mutableListOf<CartItem>()
        for(cartItem in cartItemList.value!!){
            if(cartItem.selected){
                paymentList.add(cartItem)
            }
        }

        paymentItemList.value = paymentList
    }

    fun addToCart(item : Product, quantity : Int){
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

    private fun addQuantity(item: Product, list: MutableList<CartItem>, quantity: Int) : Boolean{
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

    fun isCartNotEmpty() : Boolean
        = cartItemList.value!!.isNotEmpty()

    fun isCartHaveSelected() : Boolean{
        for(cartItem in cartItemList.value!!){
            if(cartItem.selected){
                return true
            }
        }

        return false
    }



    fun createProductOrder(info : Order) : ProductOrder{
        val order: ProductOrder?
        val cart : MutableList<CartOrder> = mutableListOf()

        for(orderItem in paymentItemList.value!!){
            cart.add(
                CartOrder(
                orderItem.item!!.id,
                orderItem.quantity)
            )
        }

        order = info as ProductOrder
        order.cart = cart

        return order
    }

    fun createProductOrder(id : String,customerPickup: CustomerPickup,deliveryInfo: DeliveryInfo,
                           isdelivery : Boolean,note : String,paymentMethod : String) : ProductOrder{
        val order: ProductOrder?
        val cart : MutableList<CartOrder> = mutableListOf()

        for(orderItem in paymentItemList.value!!){
            cart.add(
                CartOrder(
                    orderItem.item!!.id,
                    orderItem.quantity)
            )
        }

        order = ProductOrder().apply {
            customerID = id
            customerName = customerPickup.name
            customerPhone = customerPickup.phone
            this.isdelivery = if(isdelivery) "yes" else "no"
            address = if(isdelivery) deliveryInfo.address else ""
            this.note = note
            payment = paymentMethod
            this.cart = cart
        }

        return order
    }

    fun sendProductOrder(order: ProductOrder){
        viewModelScope.launch {
            try {
                val response = repository.sendProductOrder(order)
                this@ShoppingViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }



}