package com.example.tugas10

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            // Hapus data dari SharedPreferences
            val prefManager = PrefManager(it)
            prefManager.clear()

            // Arahkan ke halaman login
            val loginIntent = Intent(it, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(loginIntent)
        }
    }
}
