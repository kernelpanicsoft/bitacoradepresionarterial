package notifications.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ga.kps.bitacoradepresionarterial.AlarmBroadcastReceiver
import java.util.*

class AlarmsManagerForReminder(val context: Context) {
    private var alarmMgr : AlarmManager ? = null
    lateinit var alarmIntent: PendingIntent

    fun createAlarmForNotification(hourOfDay: Int, minute: Int, requestCode: Int){
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).let {intent ->
            intent.putExtra("title","Hola desde manager")
            intent.putExtra("content", "djkgfk")
            PendingIntent.getBroadcast(context, requestCode,intent,PendingIntent.FLAG_ONE_SHOT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    fun cancelAlarm(){
        alarmMgr?.cancel(alarmIntent)
    }

}