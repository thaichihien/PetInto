package com.mobye.petinto.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import com.mobye.petinto.R
import com.mobye.petinto.ui.MainActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class Utils {
    companion object{
        fun formatToLocalDate(ourDate: String) : String
            = try {
                val value: Date = Date.from(Instant.parse(ourDate))
                val dateFormatter = SimpleDateFormat("HH:mm MM-dd-yyyy") //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                dateFormatter.format(value)
                //Log.d("ourDate", ourDate);
            } catch (e: Exception) {
                "00-00-0000 00:00"
            }

        fun formatMoneyVND(amount : Int) : String
            = "%,d đ".format(amount)


        fun getLoadingDialog(activity: Activity)
                = (activity as MainActivity).dialog

        fun createNotificationDialog(context : Context)
                =  Dialog(context).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.custom_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }

        fun createConfirmDialog(context : Context,title : String,message : String,listener : () -> Unit) : AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setMessage(message)
                setTitle(title)
                setPositiveButton("Yes") { _, _ ->
                    listener()
                }
                setNegativeButton("No") { _, _ ->
                    //nothing
                }
            }
            return builder.create()
        }


        fun getProductTypeIndex(type : String) : Int
            = when(type){
                "Tất cả" -> 0
                "Dog" -> 1
                "Cat" -> 2
                "Rabbit" -> 3
                "Mouse" -> 4
                "Bird" -> 5
                "Fish" -> 6
                else -> 0
            }


    }
}