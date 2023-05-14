package com.mobye.petinto.database

import com.mobye.petinto.models.CartItem
import com.mobye.petinto.models.Product
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

object  CartItemDatabase {

    private const val REALM_NAME = "CART_DB"
    private val realm : Realm by lazy {
        Realm.open(
            RealmConfiguration.Builder(
                setOf(CartItem::class,Product::class)
            ).name(REALM_NAME).build()
        )
    }
    fun createOrUpdate(cartItem: CartItem){
        realm.writeBlocking {
            copyToRealm(cartItem,UpdatePolicy.ALL)
        }
    }
    fun remove(cartItem: CartItem){
        realm.writeBlocking {
            val cartItemDB =findLatest(realm.query<CartItem>("item.id == $0", cartItem.item!!.id).find().first())
            if(cartItemDB != null){
                delete(cartItemDB)
            }
        }
    }

    fun removeAll(){
        realm.writeBlocking {
            val cart: RealmResults<CartItem> = this.query<CartItem>().find()
            delete(cart)
        }
    }

    fun update(cartItem: CartItem){
        realm.writeBlocking {
            val cartItemDB = findLatest(realm.query<CartItem>("item.id == $0", cartItem.item!!.id).find().first())
            cartItemDB?.apply {
               quantity = cartItem.quantity
            }
        }
    }

    fun getAllCart() : List<CartItem>
            = realm.copyFromRealm(realm.query<CartItem>().find())
}