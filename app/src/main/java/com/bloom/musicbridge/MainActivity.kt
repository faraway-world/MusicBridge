package com.bloom.musicbridge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNotificationServiceEnabled()) {
            // Open notification access settings only if not granted
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        setContentView(R.layout.activity_main)

        val ipInput = findViewById<EditText>(R.id.ipInput)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val statusText = findViewById<TextView>(R.id.statusText)

        val prefs = getSharedPreferences("MusicBridgePrefs", Context.MODE_PRIVATE)
        val savedIp = prefs.getString("server_ip", "")
        ipInput.setText(savedIp)

        // Start service with last saved IP
        startForegroundServiceWithStatus(savedIp ?: "")

        saveButton.setOnClickListener {
            val ip = ipInput.text.toString().trim()
            if (ip.isNotEmpty()) {
                prefs.edit().putString("server_ip", ip).apply()
                startForegroundServiceWithStatus(ip)
                statusText.text = "Status: Configured ($ip)"
            }
        }

    }

    private fun startForegroundServiceWithStatus(ip: String) {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        serviceIntent.putExtra("ip_address", ip)
        startService(serviceIntent)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        if (!flat.isNullOrEmpty()) {
            val names = flat.split(":")
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
