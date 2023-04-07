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

    fun getUser(id: String) : Customer?{
        return try {
            realm.query<Customer>("id == $0", id).first().find()
        }catch (e : Exception){
            null
        }
    }


    fun saveUser(user: Customer) {
        realm.writeBlocking {

            val userDB = try {
                findLatest(realm.query<Customer>("id == $0",user.id).find().first())
            }catch(_ : Exception) {
                null
            }

            if(userDB != null){
                userDB.apply {
                    name = user.name
                    phone = user.phone
                    image = user.image
                }
                copyToRealm(userDB,UpdatePolicy.ALL)
            }else{
                copyToRealm(user,UpdatePolicy.ALL)
            }
        }
    }

    fun getCustomerPickup(id : String) : CustomerPickup?
            = realm.query<CustomerPickup>("customerID == $0", id).first().find()

    fun updateCustomerPickup(customerPickup: CustomerPickup){
        realm.writeBlocking {
            val customerPickupDB  = query<CustomerPickup>("customerID == $0", customerPickup.customerID).first().find()
            customerPickupDB?.apply {
                name = customerPickup.name
                phone = customerPickup.phone
            }
                ?: Log.e(REALM_NAME,"customerPickupDB is null")
        }
    }

    fun createCustomerPickup(customerPickup: CustomerPickup){
        realm.writeBlocking {
            copyToRealm(customerPickup)
        }
    }

    fun getAllDeliveryAddress(id : String) : List<DeliveryInfo>
        = realm.copyFromRealm(realm.query<DeliveryInfo>("customerID = $0",id).find())

    fun updateDeliveryAddress(deliveryInfo: DeliveryInfo){
        realm.writeBlocking {
            val deliveryInfoDB = try {
                findLatest(realm.query<DeliveryInfo>("id == $0",deliveryInfo.id).find().first())
            }catch(_ : Exception) {
                null
            }

            if(deliveryInfoDB != null){
                deliveryInfoDB.apply {
                    address = deliveryInfo.address
                    isDefault = deliveryInfo.isDefault
                    name = deliveryInfo.name
                    phone = deliveryInfo.phone
                }
                copyToRealm(deliveryInfoDB,UpdatePolicy.ALL)
            }else{
                copyToRealm(deliveryInfo,UpdatePolicy.ALL)
            }
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

    fun getDefaultDeliveryAddress(id : String)
        = realm.query<DeliveryInfo>("isDefault == $0 AND customerID == $1", true,id).first().find()


    // Pet database
    fun getPetList(id : String)
        = realm.copyFromRealm(realm.query<PetInfo>("customerID = $0",id).find())


    fun updatePet(pet : PetInfo){
        realm.writeBlocking {
            val petDB = try {
                findLatest(realm.query<PetInfo>("idLocal == $0",pet.id).find().first())
            }catch(_ : Exception) {
                null
            }

            if(petDB != null){
                petDB.apply {
                    name = pet.name
                    type = pet.type
                    image = pet.image
                    gender = pet.gender
                    age = pet.age
                    variety = pet.variety
                    vaccine = pet.vaccine
                    weight = pet.weight
                    color = pet.color
                }
                copyToRealm(petDB,UpdatePolicy.ALL)
            }else{
                copyToRealm(pet,UpdatePolicy.ALL)
            }
        }
    }

    fun deletePet(pet: PetInfo){
        realm.writeBlocking {
            val petDB = try {
                findLatest(realm.query<PetInfo>("idLocal == $0",pet.id).find().first())
            }catch(_ : Exception) {
                null
            }

            if(petDB != null){
                delete(petDB)
            }
        }
    }


}