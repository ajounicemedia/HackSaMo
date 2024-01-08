package com.example.god_of_teaching.view.nav.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.god_of_teaching.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val senderUid = message.data["sender_uid"] ?: return//상대방 uid호출
        // Shared Preferences 인스턴스 생성
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val preferenceUtils = PreferenceUtils(sharedPreferences)

                if (!preferenceUtils.isBlackNotification(senderUid)) //알람처리 확인 //
                {
                    val name = "학사모 채팅 알림"
                    val descriptionText = "새로운 메시지가 도착했습니다"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel(
                        getString(R.string.default_notification_channel_id), name, importance).apply {
                        description = descriptionText
                    }
                    // Register the channel with the system
                    val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                    val title = message.notification?.title ?: "학사모"
                    val body = message.notification?.body
                    val notificationBuilder = NotificationCompat.Builder(applicationContext,getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(body)


                    notificationManager.notify(0,notificationBuilder.build())
                }





    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}