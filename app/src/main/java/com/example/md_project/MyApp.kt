package com.example.md_project

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.dataStore


val Context.dataStore by dataStore("settings.json", AppSettingsSerializer) //Provides the AppSettings to every activity
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel(){ //Creates a notification channel if the Android version is 8.0 or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("Notification", "Channel created")
            val channel = NotificationChannel(
                "test1",
                "test1",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}



