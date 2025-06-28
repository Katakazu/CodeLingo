package com.example.codelingo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.codelingo.data.model.AppUser

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): AppUser?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): AppUser?

    @Insert
    suspend fun insertUser(user: AppUser): Long

    @Update
    suspend fun updateUser(user: AppUser)
}