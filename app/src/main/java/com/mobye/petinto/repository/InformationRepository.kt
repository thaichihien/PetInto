package com.mobye.petinto.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mobye.petinto.R
import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.database.AccountInfoDatabase
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import io.realm.kotlin.types.RealmUUID
import retrofit2.Response

class InformationRepository {

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

    suspend fun sendUser(user: Customer) : Response<ApiResponse<Any>>
        = RetrofitInstance.api.sendUser(user)

    suspend fun sendGoogleUser(user: Customer) : Response<ApiResponse<Any>>
            = RetrofitInstance.api.sendUser(user,true)

    suspend fun getUser(id : String) : Response<ApiResponse<Customer>>
        = RetrofitInstance.api.getUser(id)

    suspend fun getUserLocal(id: String) : Customer?
        = AccountInfoDatabase.getUser(id)

    suspend fun saveUserLocal(user: Customer){
        AccountInfoDatabase.saveUser(user)
    }

    suspend fun createCustomerPickup(customerPickup: CustomerPickup){
        AccountInfoDatabase.createCustomerPickup(customerPickup)
    }

    suspend fun getCustomerPickup(id : String)
        = AccountInfoDatabase.getCustomerPickup(id)

    suspend fun updateCustomerPickup(customerPickup: CustomerPickup){
        AccountInfoDatabase.updateCustomerPickup(customerPickup)
    }

    suspend fun getAllDeliveryAddress(id : String) : List<DeliveryInfo>
        = AccountInfoDatabase.getAllDeliveryAddress(id)

    suspend fun updateDeliveryAddress(deliveryInfo: DeliveryInfo){
        AccountInfoDatabase.updateDeliveryAddress(deliveryInfo)
    }

    suspend fun updateDefaultDeliveryAddress(id : RealmUUID,isDefault : Boolean){
        AccountInfoDatabase.updateDefaultDeliveryAddress(id,isDefault)
    }

    suspend fun updateDefaultDeliveryAddress(id: RealmUUID){
        AccountInfoDatabase.updateDefaultDeliveryAddress(id)
    }

    suspend fun getDefaultDeliveryAddress(id : String)
        = AccountInfoDatabase.getDefaultDeliveryAddress(id)
}
