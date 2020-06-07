package notifications.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(DEFAULT_NOTIFICATION_CHANEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNofiticationChannelForReports(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "BPA_notification_channel_reports"
            val descriptionText = "Allows BPA display notifications for reports"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(DEFAULT_NOTIFICATION_CHANEL_ID, name, importance).apply {
                description = descriptionText
            }

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

    fun sendNotificationForReportCreation(title: String, content: String){
        val notificationBuilder= NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANEL_ID).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(R.drawable.ic_bpa_notification)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setOnlyAlertOnce(true)
            setOngoing(true)

        }
        val notificationID = createNotificationID()
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0

        NotificationManagerCompat.from(context).apply{
            notificationBuilder.setProgress(PROGRESS_MAX,PROGRESS_CURRENT,false)
            notify(notificationID, notificationBuilder.build())

            //Crea el reporte asyncronamente
            Thread(Runnable {
                for(i in 1..10){
                    Thread.sleep(1000)
                    notificationBuilder.setProgress(PROGRESS_MAX,i*10, false)
                    notificationBuilder.setOngoing(false)
                    notify(notificationID, notificationBuilder.build())
                    Log.d("Hilo", i.toString())
                }
            }).start()




            notificationBuilder.setContentText(context.getString(R.string.reporte_creado))
                .setProgress(0,0,false)
            notify(notificationID, notificationBuilder.build())
        }
    }

    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss",Locale.US).format(date))
    }
}