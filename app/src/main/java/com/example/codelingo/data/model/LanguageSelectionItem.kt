package com.example.codelingo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "programming_languages")
data class ProgrammingLanguage(
    @PrimaryKey
    val id: Int,
    val name: String,
    val iconName: String,
    var isSelected: Boolean = false
) : Serializable

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val languageId: Int,
    val order: Int,
    var isUnlocked: Boolean = false,
    var isCompleted: Boolean = false,
    val xpReward: Int = 10,
    val content: String = ""
) : Serializable

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val lessonId: Int,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val completedAt: Long? = null
) : Serializable