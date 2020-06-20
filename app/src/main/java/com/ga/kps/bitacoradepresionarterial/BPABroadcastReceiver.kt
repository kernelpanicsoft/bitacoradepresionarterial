package com.ga.kps.bitacoradepresionarterial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import notifications.system.NotificationsManagerForReminders

class BPABroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED"){
            val notificationsManager = NotificationsManagerForReminders(context)
            notificationsManager.sendNotificationForReminder("HOla desde el reinicio", "Se reinició el telefono")
        }

    }
}