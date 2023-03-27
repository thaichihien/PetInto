package com.mobye.petinto.models

import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import kotlinx.parcelize.Parcelize

@Parcelize
class ShoppingItem(
    var id : String = "",
    var name : String = "",
    var price : Int = 0,
    var type : String = "",
    var stock: Int = 0,
    var detail: String = "",
    var image: Int = 0
) : Parcelable,RealmObject{
    constructor() : this("","",0,"",0,"",0){
    }

}
