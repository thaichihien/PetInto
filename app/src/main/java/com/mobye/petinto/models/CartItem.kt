package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject
class CartItem (
    var item: Product?,
    var quantity : Int,
    var selected : Boolean = false
) : RealmObject{
    constructor() : this(Product(),0,false)
}