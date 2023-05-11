package com.mobye.petinto.repository

import com.mobye.petinto.api.RetrofitInstance
import com.mobye.petinto.database.AccountInfoDatabase
import com.mobye.petinto.models.Customer
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.models.apimodel.ApiResponse
import com.mobye.petinto.models.apimodel.Report
import io.realm.kotlin.types.RealmUUID
import retrofit2.Response

class InformationRepository :IRepository {



    suspend fun sendUser(user: Customer) : Response<ApiResponse<Any>>
        = RetrofitInstance.api.sendUser(user)

    suspend fun sendGoogleUser(user: Customer) : Response<ApiResponse<Any>>
            = RetrofitInstance.api.sendUser(user,true)

    suspend fun getUser(id: String, token: Map<String, String>) : Response<ApiResponse<Customer>>
        = RetrofitInstance.api.getUser(id,token)

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


    //Pet
    suspend fun getPetList(id: String)
        = AccountInfoDatabase.getPetList(id)

    suspend fun updatePet(petInfo: PetInfo){
        AccountInfoDatabase.updatePet(petInfo)
    }

    suspend fun deletePet(petInfo: PetInfo){
        AccountInfoDatabase.deletePet(petInfo)
    }


    suspend fun getOrderHistory(id: String)
        = RetrofitInstance.api.getOrderHistory(id)

    suspend fun getPetOrderHistory(id : String)
        = RetrofitInstance.api.getPetOrderHistory(id)

    suspend fun sendReport(report: Report)
        = RetrofitInstance.api.sendReport(report)

}
