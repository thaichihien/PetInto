package com.mobye.petinto.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetInfo (
    val id : String,
    var name : String,
    var price : Int,
    var type : String,
    var condition: String,
    var image: Int,
    var gender: String,
    var age: Int,
    var vaccine: Boolean,
    var variety: String,
    var weight: Double,
    var color: String) : Parcelable