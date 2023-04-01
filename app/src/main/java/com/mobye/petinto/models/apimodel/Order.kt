package com.mobye.petinto.models.apimodel

open class Order(
    var customerID : String = "",
    var isdelivery : String = "",
    var address : String = "",
    var note : String = "",
    var payment : String = "",
    var customerName : String = "",
    var customerPhone : String = "",
)