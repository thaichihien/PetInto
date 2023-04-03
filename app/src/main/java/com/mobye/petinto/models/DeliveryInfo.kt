package com.mobye.petinto.models

import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class DeliveryInfo(
    var address: String = "",
    var isDefault : Boolean = false,
    var customerID : String = "",
    var name: String = "",
    var phone: String = "",
    @PrimaryKey
    var id : @RawValue RealmUUID = RealmUUID.random()
) : RealmObject, Parcelable {
    constructor() : this("",false,"","","")
}