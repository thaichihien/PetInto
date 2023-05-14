package com.mobye.petinto.models

import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Parcelize
class PetInfo (
    var id : String = "",
    var name : String = "",
    var price : Int = 0,
    var type : String = "",
    var image: String = "",
    var gender: String = "",
    var age: Int = 0,
    var vaccine: Int = 0,
    var variety: String = "",
    var weight: String = "",
    var color: String = "",
    var customerID : String = "",
    @PrimaryKey
    var idLocal : @RawValue RealmUUID = RealmUUID.random()
    ) : Parcelable,RealmObject{
        constructor() : this("","",0,"",
        "","",0,0,"","",
            "","")
    }