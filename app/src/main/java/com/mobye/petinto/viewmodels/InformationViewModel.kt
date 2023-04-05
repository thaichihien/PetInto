package com.mobye.petinto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.launch

class InformationViewModel(val repository: InformationRepository) : ViewModel(){

    val TAG = "InformationViewModel"
    val myPetList : MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }
    val user : MutableLiveData<Customer> by lazy { MutableLiveData() }
    val response : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }


    //Payment
    val customerPickup : MutableLiveData<CustomerPickup?> by lazy { MutableLiveData(null) }
    val deliveryList : MutableLiveData<List<DeliveryInfo>> by lazy { MutableLiveData(listOf()) }
    val defaultDeliveryAddress : MutableLiveData<DeliveryInfo?> by lazy { MutableLiveData(null) }

    fun addPet(pet : PetInfo){
        var list = myPetList.value!!.toMutableList()
        list.add(pet)
        myPetList.value = list
    }

    fun getUser(id : String){
        // get user data by id from backend (id,email,name)
        viewModelScope.launch {
            try {
                val response = repository.getUser(id)
                if(response.isSuccessful){
                    val userFromBE = response.body()!!.body!!
                    val userFromLocal = repository.getUserLocal(userFromBE.id)
                    if(userFromLocal != null){
                        this@InformationViewModel.user.value = userFromLocal
                    }else{
                        repository.saveUserLocal(userFromBE)
                        this@InformationViewModel.user.value = userFromBE
                    }

                }else{
                    //505 server error
                    Log.e(TAG,response.body()!!.error)
                }

            }catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }

    }

    fun getUserID() = user.value!!.id

    fun clearUser(){
        user.value = null
    }


    // Send user data to database
    fun sendUser(user: Customer){
        // send id,email,username
        viewModelScope.launch {
         try {
             val response = repository.sendUser(user)
             this@InformationViewModel.response.value = response.body()
         }catch (e: Exception){
             // no internet connection
         }
        }
    }

    fun sendGoogleUser(user: Customer){
        // send id,email,username
        viewModelScope.launch {
            try {
                val response = repository.sendGoogleUser(user)
                this@InformationViewModel.response.value = response.body()
            }catch (e: Exception){
                // no internet connection
            }
        }
    }


    fun getCustomerPickup(){
        viewModelScope.launch {
            var customerPickupLocal = repository.getCustomerPickup(this@InformationViewModel.user.value!!.id)
            if(customerPickupLocal == null){
                Log.e(TAG,"customerPickup is null")
                customerPickupLocal = CustomerPickup(
                    customerID = this@InformationViewModel.user.value!!.id,
                    name = this@InformationViewModel.user.value!!.name
                )
                repository.createCustomerPickup(customerPickupLocal)
            }
            customerPickup.value = customerPickupLocal
        }
    }

    fun updateCustomerPickup(customerPickup: CustomerPickup){
        viewModelScope.launch {
            customerPickup.customerID = getUserID()
            repository.updateCustomerPickup(customerPickup)
            this@InformationViewModel.customerPickup.value = customerPickup
        }
    }

    fun getDefaultDeliveryAddress(id: String){
        viewModelScope.launch {
            defaultDeliveryAddress.value = repository.getDefaultDeliveryAddress(id)
        }
    }

    fun getAllDeliveryAddress(id : String){
        viewModelScope.launch {
            deliveryList.value = repository.getAllDeliveryAddress(id)
        }
    }

    fun updateDeliveryAddress(deliveryInfo: DeliveryInfo){
        viewModelScope.launch{
            repository.updateDeliveryAddress(deliveryInfo)
        }
    }

    fun setDefaultDeliveryAddress(index : Int,isDefault : Boolean){
        val _deliveryList = deliveryList.value!!.toMutableList()

        _deliveryList[index].isDefault = isDefault

        deliveryList.value = _deliveryList
    }

    fun setDefaultDeliveryAddress(index : Int){
        viewModelScope.launch {
            val deliveryInfo = deliveryList.value!![index]
            repository.updateDefaultDeliveryAddress(deliveryInfo.id)
        }
    }

    fun setDefaultDeliveryAddress(id : RealmUUID){
        viewModelScope.launch {
            repository.updateDefaultDeliveryAddress(id)
        }
    }

}