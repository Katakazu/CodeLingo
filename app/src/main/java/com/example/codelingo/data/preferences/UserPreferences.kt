package com.example.codelingo.data.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("codelingo_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
        private const val KEY_LEVEL = "user_level"
        private const val KEY_XP = "user_xp"
        private const val KEY_DAYS = "user_days"
        private const val KEY_LAST_LEARN_DATE = "last_learn_date"
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setCurrentUserId(userId: Int) {
        sharedPreferences.edit().putInt(KEY_CURRENT_USER_ID, userId).apply()
    }

    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(KEY_CURRENT_USER_ID, -1)
    }

    // Statistik User
    fun setUserLevel(level: Int) {
        sharedPreferences.edit().putInt(KEY_LEVEL, level).apply()
    }
    fun getUserLevel(): Int {
        return sharedPreferences.getInt(KEY_LEVEL, 0)
    }
    fun setUserXp(xp: Int) {
        sharedPreferences.edit().putInt(KEY_XP, xp).apply()
    }
    fun getUserXp(): Int {
        return sharedPreferences.getInt(KEY_XP, 0)
    }
    fun setUserDays(days: Int) {
        sharedPreferences.edit().putInt(KEY_DAYS, days).apply()
    }
    fun getUserDays(): Int {
        return sharedPreferences.getInt(KEY_DAYS, 0)
    }

    fun setLastLearnDate(date: String) {
        sharedPreferences.edit().putString(KEY_LAST_LEARN_DATE, date).apply()
    }
    fun getLastLearnDate(): String? {
        return sharedPreferences.getString(KEY_LAST_LEARN_DATE, null)
    }
}