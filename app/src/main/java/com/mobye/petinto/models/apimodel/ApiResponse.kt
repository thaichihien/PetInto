package com.mobye.petinto.models.apimodel

class ApiResponse<T>(
    var result : Boolean,
    var reason : String,
    var error : String,
    var body : T? = null
) {
}