package com.example.codelingo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.codelingo.data.dao.UserDao
import com.example.codelingo.data.model.AppUser  // Ganti import

@Database(
    entities = [AppUser::class],  // Ganti User::class jadi AppUser::class
    version = 1,
    exportSchema = false
)
abstract class CodeLingoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: CodeLingoDatabase? = null

        fun getDatabase(context: Context): CodeLingoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeLingoDatabase::class.java,
                    "codelingo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}