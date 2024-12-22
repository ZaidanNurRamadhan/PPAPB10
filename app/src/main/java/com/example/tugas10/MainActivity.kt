package com.example.tugas10

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tugas10.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val disasterList = mutableListOf(
        Disaster(R.drawable.img, "Kadal", isBookmarked = false),
        Disaster(R.drawable.img_2, "Harimau", isBookmarked = false),
        Disaster(R.drawable.img_1, "Kucing", isBookmarked = false),
        Disaster(R.drawable.img_3, "Kuda", isBookmarked = false),
        Disaster(R.drawable.img_4, "Bunga", isBookmarked = false),
        Disaster(R.drawable.img_5, "Kura", isBookmarked = false)
    )
    private val bookmarkedList = mutableListOf<Disaster>()
    private lateinit var disasterAdapter: DisasterAdapter
    private lateinit var bookmarkedAdapter: DisasterAdapter

    private val channelId = "TEST_NOTIF"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Setup Adapter
        disasterAdapter = DisasterAdapter(disasterList) { disaster -> toggleBookmark(disaster) }
        bookmarkedAdapter = DisasterAdapter(bookmarkedList) { disaster -> toggleBookmark(disaster) }

        // Setup RecyclerView
        binding.recyclerViewDisaster.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewDisaster.adapter = disasterAdapter

        binding.recyclerViewBookmark.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewBookmark.adapter = bookmarkedAdapter

        // Setup TabLayout
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showAllDisasters()
                    1 -> showBookmarks()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Bookmarks"))

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createLogoutNotification()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        1
                    )
                }
            } else {
                createLogoutNotification()
            }
        }
    }

    private fun toggleBookmark(disaster: Disaster) {
        disaster.isBookmarked = !disaster.isBookmarked
        if (disaster.isBookmarked) {
            bookmarkedList.add(disaster)
        } else {
            bookmarkedList.remove(disaster)
        }
        disasterAdapter.notifyDataSetChanged()
        bookmarkedAdapter.notifyDataSetChanged()
    }

    private fun showAllDisasters() {
        binding.recyclerViewDisaster.visibility = View.VISIBLE
        binding.recyclerViewBookmark.visibility = View.GONE
    }

    private fun showBookmarks() {
        binding.recyclerViewDisaster.visibility = View.GONE
        binding.recyclerViewBookmark.visibility = View.VISIBLE
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun createLogoutNotification() {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Logout Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val logoutIntent = Intent(this, NotifReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            logoutIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Logout")
            .setContentText("Klik untuk logout")
            .setSmallIcon(R.drawable.baseline_music_note_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createLogoutNotification()
        } else {
            Toast.makeText(this, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }
}
