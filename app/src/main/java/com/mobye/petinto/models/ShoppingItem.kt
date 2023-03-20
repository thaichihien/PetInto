package com.mobye.petinto.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingItem(
    var name : String,
    var price : Int,
    var type : String,
    var stock: Int,
    var detail: String,
    var image: Int
) : Parcelable
