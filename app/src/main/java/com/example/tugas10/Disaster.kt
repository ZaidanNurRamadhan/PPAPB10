package com.example.tugas10

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Disaster(
//cek
    val imageResource: Int,
    val name: String,
    var isBookmarked: Boolean = false
)


