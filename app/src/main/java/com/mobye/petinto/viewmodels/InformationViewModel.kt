package com.mobye.petinto.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.OrderHistory
import com.mobye.petinto.models.apimodel.PetOrderHistory
import com.mobye.petinto.models.apimodel.Report
import com.mobye.petinto.repository.InformationRepository
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class InformationViewModel(val repository: InformationRepository) : ViewModel(){

    val TAG = "InformationViewModel"
    val myPetList : MutableLiveData<List<PetInfo>> by lazy { MutableLiveData(listOf()) }
    val user : MutableLiveData<Customer> by lazy { MutableLiveData() }
    val responseAPI : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }

    //Payment
    val customerPickup : MutableLiveData<CustomerPickup?> by lazy { MutableLiveData(null) }
    val deliveryList : MutableLiveData<List<DeliveryInfo>> by lazy { MutableLiveData(listOf()) }
    val defaultDeliveryAddress : MutableLiveData<DeliveryInfo?> by lazy { MutableLiveData(null) }

    val customerPickupInfo get() = customerPickup.value
    val deliveryAddressInfo get() = defaultDeliveryAddress.value

    //History
    val orderHistoryList : MutableLiveData<List<OrderHistory>> by lazy { MutableLiveData(listOf()) }

    //History Pet
    val orderPetHistoryList : MutableLiveData<List<PetOrderHistory>> by lazy { MutableLiveData(listOf()) }

    fun getPetList(){
        viewModelScope.launch() {
            myPetList.value = repository.getPetList(getUserID())
        }
    }
    fun addPet(pet : PetInfo){
        val list = myPetList.value!!.toMutableList()
        pet.customerID = getUserID()
        list.add(pet)
        viewModelScope.launch {
            repository.updatePet(pet)
        }
        //myPetList.value = list
    }

    fun updatePet(pet : PetInfo, index: Int){
        val list = myPetList.value!!.toMutableList()
        pet.customerID = getUserID()
        pet.idLocal = list[index].idLocal
        viewModelScope.launch {
            repository.updatePet(pet)
        }
        //myPetList.value = list
    }

    fun deletePet(pet: PetInfo,index : Int){
        val list = myPetList.value!!.toMutableList()
        list.removeAt(index)
        viewModelScope.launch {
            repository.deletePet(pet)
        }
        myPetList.value = list
    }

    fun getPetGenre(index: Int)
        = myPetList.value!![index].type
    fun getPet(index: Int)
        = myPetList.value!![index]


    fun getUser(id : String,token : String,context : Context){
        // get user data by id from backend (id,email,name)
        viewModelScope.launch {
            try {
                val tokenSend = mapOf<String,String>(Pair("token",token))
                val response = repository.getUser(id,tokenSend)
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
                    this@InformationViewModel.responseAPI.value = ApiResponse.convertToAny(response.body()!!)
                    Log.e(TAG, response.body()!!.error)

                }

            }catch (e : SocketTimeoutException){
                Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show()
                Log.e(TAG,e.toString())
            }
            catch (e: Exception){
                // no internet connection
                Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show()
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
             this@InformationViewModel.responseAPI.value = response.body()
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
                this@InformationViewModel.responseAPI.value = response.body()
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

    fun updateUserInfo(user : Customer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserLocal(user)
        }
        this.user.value = user
    }

    fun getOrderHistory(){
        viewModelScope.launch {
            try {
                val response = repository.getOrderHistory(getUserID()).body()!!
                if(response.result){
                    orderHistoryList.value = response.body
                }else{
                    //Server error
                }

            }catch (e: Exception) {
                // no internet connection
            }
        }
    }

    fun getPetOrderHistory(){
        viewModelScope.launch {
            try {
                val response = repository.getPetOrderHistory(getUserID()).body()!!
                if(response.result){
                    orderPetHistoryList.value = response.body
                }else{
                    //Server error
                }

            }catch (e: Exception) {
                // no internet connection
            }
        }
    }

    fun sendReport(commentReport : String){
        viewModelScope.launch {
            try {
                val report = Report(
                    comment = commentReport,
                    CustomerId = this@InformationViewModel.getUserID()
                )


                val response = repository.sendReport(report)
               if(response.isSuccessful){
                  this@InformationViewModel.responseAPI.value =response.body()
               }else{
                   Log.e(TAG,response.errorBody().toString())
               }

            }catch (e: Exception) {
                // no internet connection
            }
        }
    }
}