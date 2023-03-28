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
            "Brown"),
        PetInfo("PET10002",
            "British Golden Cat",
            15000000,
            "Cat",
            "Sold out",
            R.drawable.cat,
            "Female",
            2,
            false,
            "Golden",
            1.2,
            "Yellow")
    )
}
