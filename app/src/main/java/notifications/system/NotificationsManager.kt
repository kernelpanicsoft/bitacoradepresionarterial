package notifications.system

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ga.kps.bitacoradepresionarterial.R
import com.ga.kps.bitacoradepresionarterial.ReportListActivity
import helpers.DEFAULT_NOTIFICATION_CHANEL_ID
import reports.ReportBuilder
import java.text.SimpleDateFormat
import java.util.*

class NotificationsManager(val application: Application) {

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
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun getNotificationBuilder(title: String, content: String) : NotificationCompat.Builder{
        val builder = NotificationCompat.Builder(application, DEFAULT_NOTIFICATION_CHANEL_ID)
            .setSmallIcon(R.drawable.ic_bpa_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        return builder
    }

    fun sendNotificationForReminder(title: String, content: String){
        val notificationBuilder = getNotificationBuilder(title,content)
        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(createNotificationID(),notificationBuilder.build())
    }

    fun sendNotificationForReportCreation(title: String, content: String, fileName: String){
        val notificationID = createNotificationID()
        val notificationBuilder= NotificationCompat.Builder(application, DEFAULT_NOTIFICATION_CHANEL_ID).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(R.drawable.ic_bpa_notification)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setOnlyAlertOnce(true)
            setOngoing(true)
        }

        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0

        NotificationManagerCompat.from(application).apply{
            notificationBuilder.setProgress(PROGRESS_MAX,PROGRESS_CURRENT,false)
            notify(notificationID, notificationBuilder.build())

            //Crea el reporte asyncronamente
            Thread(Runnable {
               val reportBuilder = ReportBuilder(application)
                reportBuilder.setup()
                reportBuilder.createPDF(fileName)

                notificationBuilder.setContentTitle(application.getString(R.string.reporte_creado))
                    .setContentText(application.getString(R.string.toca_aqui_abrir_reportes))
                    .setProgress(0,0,false)

                val notificationIntent = Intent(application, ReportListActivity::class.java)
                val notificationPendingIntent = PendingIntent.getActivity(application, notificationID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                notificationBuilder.setContentIntent(notificationPendingIntent)
                notify(notificationID, notificationBuilder.build())

            }).start()

        }
    }

    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss",Locale.US).format(date))
    }
}