package com.mobye.petinto.models.apimodel

import com.mobye.petinto.models.PetInfo

class HotelBooking(
    var checkInDate : String = "",
    var checkOutDate : String = "",
    var petInfo: PetInfo
) : Booking() {
}