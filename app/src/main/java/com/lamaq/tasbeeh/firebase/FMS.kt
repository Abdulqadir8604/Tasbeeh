package com.lamaq.tasbeeh.firebase

import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toIcon
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lamaq.tasbeeh.R

class FMS: FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        var title = message.notification?.title
        var body = message.notification?.body
        var image = message.notification?.imageUrl

        var notificationBuilder = NotificationCompat.Builder(this, "tasbeeh_aq")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image?.let { it }?.toIcon() ?: return))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .extend { builder ->
                builder.setSmallIcon(R.drawable.ic_launcher)
            }
            .setColor(resources.getColor(R.color.primary))
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.click))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationBuilder.build().let {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, it)
        }

        super.onMessageReceived(message)
    }
}