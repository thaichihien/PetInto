package com.mobye.petinto.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobye.petinto.R
import com.mobye.petinto.database.NotificationDatabase
import com.mobye.petinto.models.Notification
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils

class PetIntoMessagingService : FirebaseMessagingService() {

    private val TAG = "PetIntoMessagingService"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            //Log.d(TAG, "title: ${remoteMessage.data["title"]}")
            val body = remoteMessage.data["body"]
            val title = remoteMessage.data["title"]
            val type = remoteMessage.data["type"]

            val notification = Notification(
                title!!,body!!,type!!,
                Utils.getCurrentTimeString()
            )
            NotificationDatabase.createNotification(notification)

            showNotification(title,body,type)
        }
    }

    private fun showNotification(title: String, body: String,type : String) {
        val channelID = "PetIntoChannel"
        val notificationID = 111111

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelID,"PetInto",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is PetInto notification"
            }

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this,channelID)
            .setSmallIcon(R.mipmap.logo_app)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if(type == "BOOKING"){
            val pendingIntent = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.bookingFragment)
                .setComponentName(MainActivity::class.java)
                .createPendingIntent()

            builder.setContentIntent(pendingIntent)
        }


        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(
                    this@PetIntoMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(TAG,"Permission denied")
                return
            }
            notify(notificationID,builder.build())
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}