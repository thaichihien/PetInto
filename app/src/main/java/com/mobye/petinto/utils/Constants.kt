package com.mobye.petinto.utils

class Constants {
    companion object{
        const val SHIPPING_FEE = 30000
        const val NORMAL_ROOM_COST = 200000
        const val VIP_ROOM_COST = 500000

        val productTypeList = listOf<String>(
            "Tất cả",
            "Dog","Cat","Rabbit","Mouse","Bird",
            "Fish"
        )

        val genderList = listOf(
            "Tất cả",
            "Male","Female","Bigender","Polygender"
        )


    }
}