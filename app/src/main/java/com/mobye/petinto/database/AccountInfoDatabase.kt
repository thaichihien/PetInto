package com.mobye.petinto.database

import android.util.Log
import com.mobye.petinto.models.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmUUID

object AccountInfoDatabase {

    private const val REALM_NAME = "INFO_DB"
    private val realm : Realm by lazy {
        Realm.open(
            RealmConfiguration.Builder(
                setOf(Customer::class,DeliveryInfo::class,PetInfo::class,CustomerPickup::class)
            ).name(REALM_NAME).build()
        )
    }

    fun getUser(id: String) : Customer?
        = realm.query<Customer>("id == $0", id).first().find()

    fun saveUser(user: Customer) {
        realm.writeBlocking {
            copyToRealm(user,UpdatePolicy.ALL)
        }
    }

    fun getCustomerPickup(id : String) : CustomerPickup?
            = realm.query<CustomerPickup>("id == $0", id).first().find()

    fun updateCustomerPickup(customerPickup: CustomerPickup){
        realm.writeBlocking {
            copyToRealm(customerPickup,UpdatePolicy.ALL)
        }
    }

    fun getAllDeliveryAddress(id : String) : List<DeliveryInfo>
        = realm.copyFromRealm(realm.query<DeliveryInfo>("customerID = $0",id).find())

    fun updateDeliveryAddress(deliveryInfo: DeliveryInfo){
        realm.writeBlocking {
            copyToRealm(deliveryInfo,UpdatePolicy.ALL)
        }
    }

    fun updateDefaultDeliveryAddress(id : RealmUUID,isDefault: Boolean){
        realm.writeBlocking {
            val deliveryInfo  = query<DeliveryInfo>("id == $0", id).first().find()
            if(deliveryInfo != null){
                deliveryInfo.isDefault = isDefault
            }else{
                Log.e(REALM_NAME,"deliveryInfo is null")
            }
        }
    }

    fun updateDefaultDeliveryAddress(id : RealmUUID){
        realm.writeBlocking {
            val deliveryInfoPrevious  = query<DeliveryInfo>("isDefault == $0", true).first().find()
            val deliveryInfo  = query<DeliveryInfo>("id == $0", id).first().find()

            if(deliveryInfoPrevious != null){
                deliveryInfoPrevious.isDefault = false
            }

            if(deliveryInfo != null){
                deliveryInfo.isDefault = true
            }else{
                Log.e(REALM_NAME,"deliveryInfo is null")
            }
        }
    }

    fun getDefaultDeliveryAddress()
        = realm.query<DeliveryInfo>("isDefault == $0", true).first().find()






}