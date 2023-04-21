package com.mobye.petinto.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object{
        fun formatToLocalDate(ourDate: String) : String
            = try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value: Date = formatter.parse(ourDate)
                val dateFormatter = SimpleDateFormat("HH:mm MM-dd-yyyy") //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                dateFormatter.format(value)
                //Log.d("ourDate", ourDate);
            } catch (e: Exception) {
                "00-00-0000 00:00"
            }

    }
}