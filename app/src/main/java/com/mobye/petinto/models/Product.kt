package com.mobye.petinto.models

import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import kotlinx.parcelize.Parcelize

@Parcelize
class Product(
    var id : String = "",
    var name : String = "",
    var price : Int = 0,
    var typePet : String = "",
    var stock: Int = 0,
    var detail: String = "",
    var image: String = ""
) : Parcelable,RealmObject{
    constructor() : this("","",0,"",0,"",""){
    }

}
