package com.example.codelingo.data.model

data class LeaderboardItem(
    val rank: Int,
    val name: String,
    val level: Int,
    val isCurrentUser: Boolean = false
)
