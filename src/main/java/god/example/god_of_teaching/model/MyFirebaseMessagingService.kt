package god.example.god_of_teaching.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import god.example.god_of_teaching.R


class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannelId = "YOUR_CHANNEL_ID"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(message.data["title"]) // 'data' 페이로드에서 제목 사용
            .setContentText(message.data["body"]) // 'data' 페이로드에서 본문 사용
            .setSmallIcon(R.mipmap.auth_intro)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())



    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Firebase.messaging.token.addOnCompleteListener { fcmToken ->

            val uid = Firebase.auth.currentUser?.uid

            val userFcmToken = hashMapOf("fcmToken" to fcmToken.result )
            if(uid!=null)
            {
                Firebase.firestore.collection("users").document(uid).set(userFcmToken, SetOptions.merge()).addOnFailureListener {it->
                    Log.d("버그", "MyFirebaseMessagingService에서 fcm 갱신 실패", it)
                }
            }
        }
    }

}