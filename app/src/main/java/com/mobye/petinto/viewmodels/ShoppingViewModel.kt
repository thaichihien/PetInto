package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobye.petinto.models.*
import com.mobye.petinto.models.apimodel.*
import com.mobye.petinto.repository.ShoppingRepository
import kotlinx.coroutines.flow.*
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

    private val searchQuery : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    private val petSearchQuery : MutableStateFlow<String> by lazy { MutableStateFlow("") }

    //PaymentFragment
    val response : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }


    val shopOrderList :MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }


    //Filter
     var min : String = ""
     var max : String = ""
     var type : String = ""




    val productItemList = searchQuery.flatMapLatest {query ->

        repository.getProductSource(query,min,max,type)
            .cachedIn(viewModelScope)
    }

    fun searchProduct(query : String){
        searchQuery.value = query
    }

    fun applyFilter(min : String,max : String,type : String){
        this.min = min
        this.max = max
        this.type = type

    }

    fun clearFilter(){
        this.min = ""
        this.max = ""
        this.type = ""
    }

    val petItemList =  petSearchQuery.flatMapLatest {query ->
        repository.getPetSource(query)
            .cachedIn(viewModelScope)
    }

    fun searchPet(query : String){
        petSearchQuery.value = query
    }


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
        val itemRemove = cartList.removeAt(index)
        viewModelScope.launch{
            repository.deleteCartItem(itemRemove)
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

    fun changeTotal(amount : Int){
        total.value = total.value?.plus(amount)
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
        cartItemList.value = listOf()
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

    fun clearCartAfterPayment(){
        val cart = cartItemList.value!!.toMutableList()
        val iterator = cart.iterator()
        while (iterator.hasNext()){
            val item =iterator.next()
            if(item.selected){
                iterator.remove()
                viewModelScope.launch {
                    repository.deleteCartItem(item)
                }
            }
        }

        cartItemList.value = cart
    }


    fun createPetOrder(id : String,customerPickup: CustomerPickup,deliveryInfo: DeliveryInfo,
                           isdelivery : Boolean,note : String,paymentMethod : String, petID: String) : PetOrder{
        val order: PetOrder?

        order = PetOrder().apply {
            customerID = id
            customerName = customerPickup.name
            customerPhone = customerPickup.phone
            this.isdelivery = if(isdelivery) "yes" else "no"
            address = if(isdelivery) deliveryInfo.address else ""
            this.note = note
            payment = paymentMethod
            this.petID = petID
        }

        return order
    }

    fun sendPetOrder(order: PetOrder){
        viewModelScope.launch {
            try {
                val response = repository.sendPetOrder(order)
                this@ShoppingViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }
    }



}