package com.example.tugas10

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tugas10.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val disasterList = mutableListOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Adapter
        disasterAdapter = DisasterAdapter(disasterList) { disaster -> toggleBookmark(disaster) }
        bookmarkedAdapter = DisasterAdapter(bookmarkedList) { disaster -> toggleBookmark(disaster) }

        // Setup RecyclerView
        binding.recyclerViewDisaster.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewDisaster.adapter = disasterAdapter

        binding.recyclerViewBookmark.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewBookmark.adapter = bookmarkedAdapter

        val disasterAdapter = DisasterAdapter(disasterList) { disaster ->
            disaster.isBookmarked = !disaster.isBookmarked // Ubah status bookmark
            disasterAdapter.notifyDataSetChanged() // Beritahu adapter untuk memperbarui UI
        }


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

        // Tambahkan Tab Titles
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Bookmarks"))
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
}
