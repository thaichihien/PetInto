package com.mobye.petinto.repository

import com.mobye.petinto.R
import com.mobye.petinto.models.PetInfo

class PetList {
    suspend fun getPetItems() : List<PetInfo> = listOf(
        PetInfo("PET10001",
            "Golden Dog",
            11800000,
            "Dog",
            "Available",
            R.drawable.dog,
            "Male",
            1,
            true,
            "Golden",
            1.5,
            "Brown")
    )
}
