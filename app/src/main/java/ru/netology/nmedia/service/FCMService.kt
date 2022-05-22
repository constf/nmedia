package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService: FirebaseMessagingService() {
    val gson = Gson()


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descText = getString(R.string.server_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descText
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val serializedAction = data[Action.KEY_ACTION] ?: return
        val action = Action.values().find { it.key == serializedAction } ?: return

        when (action) {
            Action.like -> handleLikeAction(data[Action.KEY_CONTENT] ?: return)
            Action.post -> handleNewPostAction(data[Action.KEY_CONTENT] ?: return)
        }
    }

    private fun handleNewPostAction(serializedContent: String) {
        val newPost: NewPostParser = gson.fromJson(serializedContent, NewPostParser::class.java)

        val words = newPost.postContent.split(' ')
        val shortText = words[0] + " " + words[1] + " " + words[2] + " " + words[3] + " " + words[4]

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_new_post, newPost.userName))
            .setContentText(shortText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(newPost.postContent))
            .build()

        NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
    }

    override fun onNewToken(token: String) {
        Log.d("onNewToken", token)
    }

    private fun handleLikeAction(serializedContent: String){
        val likeContent: LikeParse = gson.fromJson(serializedContent, LikeParse::class.java)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_user_liked, likeContent.userName, likeContent.postAuthor))
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
    }

    private companion object {
        const val CHANNEL_ID = "remote"
    }

}