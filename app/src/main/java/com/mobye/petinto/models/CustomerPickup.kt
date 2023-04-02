package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject

class CustomerPickup(
    var id : String = "",
    var name: String = "",
    var phone: String = "",
) : RealmObject{
    constructor() : this("","","")
}