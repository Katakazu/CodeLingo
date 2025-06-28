package com.example.codelingo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class AppUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Pastikan ada default value
    val username: String,
    val email: String,
    val password: String,
    val level: Int = 1,
    val experience: Int = 0,
    val streak: Int = 0,
    val gems: Int = 100,
    val isFirstTime: Boolean = true
)