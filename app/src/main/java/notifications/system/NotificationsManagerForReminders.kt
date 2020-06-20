package notifications.system

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ga.kps.bitacoradepresionarterial.MainActivity
import com.ga.kps.bitacoradepresionarterial.R
import helpers.NOTIFICATION_CHANNEL_FOR_REMINDERS_ID
import java.text.SimpleDateFormat
import java.util.*

class NotificationsManagerForReminders (val context: Context) {



    private fun getNotificationBuilder(title: String, content: String) : NotificationCompat.Builder{
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_FOR_REMINDERS_ID)
            .setSmallIcon(R.drawable.ic_bpa_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        return builder
    }

    fun sendNotificationForReminder(title: String, content: String){
        val notificationID = createNotificationID()
        val notificationBuilder = getNotificationBuilder(title,content)
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(context, notificationID, notificationIntent, PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setContentIntent(notificationPendingIntent)
        notificationManager.notify(notificationID,notificationBuilder.build())
    }
    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss", Locale.US).format(date))
    }
}