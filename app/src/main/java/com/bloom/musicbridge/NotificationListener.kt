package com.bloom.musicbridge

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName.contains("youtube.music")) {
            val extras = sbn.notification.extras
            val title = extras.getString("android.title")
            val artist = extras.getString("android.text")

            if (!title.isNullOrEmpty() && !artist.isNullOrEmpty()) {
                val songInfo = SongInfo(title, artist)
                SongSender.sendSong(songInfo)
            } else {
                Log.d("NotificationListener", "Missing song info")
            }
        }
    }
}