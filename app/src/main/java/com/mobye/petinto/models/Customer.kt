package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
class Customer(
    @PrimaryKey
    var id : String = "",
    var name: String = "",
    var email : String = "",
    var phone: String = "",
    var image: String = ""
) : RealmObject{
    constructor() : this("","","","","")
}