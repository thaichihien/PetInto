package com.mobye.petinto.models

import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import kotlinx.parcelize.Parcelize

@Parcelize
class PetInfo (
    val id : String = "",
    var name : String = "",
    var price : Int = 0,
    var type : String = "",
    var condition: String = "",
    var image: Int = 0,
    var gender: String = "",
    var age: Int = 0,
    var vaccine: Int = 0,
    var variety: String = "",
    var weight: Double = 0.0,
    var color: String = "",
    var customerID : String = ""
    ) : Parcelable,RealmObject{
        constructor() : this("","",0,"",
        "",0,"",0,0,"",
            0.0,"","")
    }