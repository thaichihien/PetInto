package com.mobye.petinto.models

import io.realm.kotlin.types.RealmObject


class Customer(
    var id : String = "",
    var name: String = "",
    var email : String = "",
    var phone: String = "",
    var image: String = ""
) : RealmObject{
    constructor() : this("","","","","")
}