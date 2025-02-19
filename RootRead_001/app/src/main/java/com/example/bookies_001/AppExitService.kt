package com.example.bookies_001

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.bookies_001.utils.SessionManager

class AppExitService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        SessionManager.clearSession(applicationContext)
        stopSelf()
    }
}