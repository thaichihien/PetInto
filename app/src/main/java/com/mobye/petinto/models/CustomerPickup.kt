package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject

open class CustomerPickup(
    var customerID : String = "",
    var name: String = "",
    var phone: String = "",
) : RealmObject{
    constructor() : this("","","")

    override fun toString() : String
        = "$name | $phone"
}