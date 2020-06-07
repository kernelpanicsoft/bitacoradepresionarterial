package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import notifications.system.NotificationsManager


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val notificationChannel = NotificationsManager(this)
        notificationChannel.createNotificationChannel()
        notificationChannel.createNofiticationChannelForReports()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}