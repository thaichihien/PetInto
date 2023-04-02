package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID

class DeliveryInfo(
    var customerID : String = "",
    var name : String = "",
    var phone : String = "",
    var address: String = "",
    var isDefault : Boolean = false,
    var id : RealmUUID = RealmUUID.random()
) : RealmObject{
    constructor() : this("","","","",false)
}