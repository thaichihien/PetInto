package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject

class CartItem (
    var item: ShoppingItem?,
    var quantity : Int,
    var selected : Boolean = false
) : RealmObject{
    constructor() : this(ShoppingItem(),0,false)
}