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
    @PrimaryKey
    var id : @RawValue RealmUUID = RealmUUID.random()
) : RealmObject, CustomerPickup(), Parcelable {
    constructor() : this("",false)
}