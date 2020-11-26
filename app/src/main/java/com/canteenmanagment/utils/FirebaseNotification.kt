package com.canteenmanagment.utils

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.canteenmanagment.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseNotification : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val builder = NotificationCompat.Builder(this, "Chanel_ID").setContentTitle(title)
                .setContentText(body)
                .setSubText("this is sub text").setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.notify(1, builder.build())
        }
    }
}