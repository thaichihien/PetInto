package com.mobye.petinto.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import kotlinx.coroutines.launch

class InformationViewModel(val repository: InformationRepository) :
    ViewModel(){

    val myPetList : MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }
    val user : MutableLiveData<Customer> by lazy { MutableLiveData() }
    val response : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }


    //Payment
    val customerPickup : MutableLiveData<CustomerPickup> by lazy { MutableLiveData() }


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
                }

            }catch (e: Exception){
                // no internet connection
            }
        }

        // find user on local database (Realm)
            //if exist -> get that user
            // else -> save user to Realm

    }

    fun getUserID() = user.value!!.id

    fun addUser(user : Customer){
        // TODO find user ID in realm


        // exists -> get this user
        // else -> create new

        this.user.value = user
    }

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

        //get response
    }


    fun getCustomerPickup(){
        viewModelScope.launch {
            var customerPickupLocal = repository.getCustomerPickup(this@InformationViewModel.user.value!!.id)
            if(customerPickupLocal == null){
                customerPickupLocal = CustomerPickup(
                    id = this@InformationViewModel.user.value!!.id,
                    name = this@InformationViewModel.user.value!!.name
                )
                repository.updateCustomerPickup(customerPickupLocal)
            }

            customerPickup.value = customerPickupLocal
        }
    }

    fun updateCustomerPickup(customerPickup: CustomerPickup){
        viewModelScope.launch {
            customerPickup.id = getUserID()
            repository.updateCustomerPickup(customerPickup)
            this@InformationViewModel.customerPickup.value = customerPickup
        }
    }


}