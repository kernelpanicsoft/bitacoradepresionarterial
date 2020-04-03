package notifications.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.content.ContextCompat.getSystemService
import com.ga.kps.bitacoradepresionarterial.R
import helpers.DEFAULT_NOTIFICATION_CHANEL_ID
import java.text.SimpleDateFormat
import java.util.*

class NotificationsManager(val context: Context) {

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BPA_notifiaction_channel"
            val descriptionText = "Allows BPA display notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DEFAULT_NOTIFICATION_CHANEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotificationBuilder(title: String, content: String) : NotificationCompat.Builder{
        val builder = NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANEL_ID)
            .setSmallIcon(R.drawable.ic_bpa_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        return builder
    }

    fun sendNotificationForReminder(title: String, content: String){
        val notificationBuilder = getNotificationBuilder(title,content)
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(createNotificationID(),notificationBuilder.build())
    }



    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss",Locale.US).format(date))
    }
}