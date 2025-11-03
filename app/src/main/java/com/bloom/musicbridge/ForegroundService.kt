package com.bloom.musicbridge

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "MusicBridgeChannel"
        const val NOTIFICATION_ID = 1
    }

    private val handler = Handler(Looper.getMainLooper())
    private var ip: String? = null
    private var baseUrl: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ip = intent?.getStringExtra("ip_address")
        baseUrl = if (!ip.isNullOrEmpty() && !ip!!.startsWith("http")) {
            "http://$ip/"
        } else {
            ip
        }

        if (!baseUrl.isNullOrEmpty()) {
            RetrofitClient.init(baseUrl!!)
        }

        startPingLoop()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showNotification("Server not configured")
    }

    private fun startPingLoop() {
        handler.post(object : Runnable {
            override fun run() {
                val service = RetrofitClient.apiService
                if (service != null) {
                    service.ping().enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            showNotification("Connected to $ip")
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            showNotification("Failed to connect to $ip")
                        }
                    })
                } else {
                    showNotification("Server not configured")
                }
                handler.postDelayed(this, 5000) // check every 5s
            }
        })
    }

    private fun showNotification(contentText: String) {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MusicBridge Running")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MusicBridge Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
