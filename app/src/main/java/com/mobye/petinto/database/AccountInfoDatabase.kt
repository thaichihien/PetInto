package com.mobye.petinto.database

import com.mobye.petinto.models.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

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






}