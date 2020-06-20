package com.ga.kps.bitacoradepresionarterial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import notifications.system.NotificationsManagerForReminders

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationsManager = NotificationsManagerForReminders(context)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        notificationsManager.sendNotificationForReminder(title!!, content!!)
    }
}