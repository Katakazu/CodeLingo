package com.example.codelingo.data.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "codelingo_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
        private const val KEY_IS_FIRST_TIME = "is_first_time"
        private const val KEY_DAILY_STREAK = "daily_streak"
        private const val KEY_LAST_LOGIN_DATE = "last_login_date"
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setCurrentUserId(userId: Int) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun setSelectedLanguage(language: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }

    fun getSelectedLanguage(): String? {
        return sharedPreferences.getString(KEY_SELECTED_LANGUAGE, null)
    }

    fun setIsFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_FIRST_TIME, isFirstTime).apply()
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_TIME, true)
    }

    fun setDailyStreak(streak: Int) {
        sharedPreferences.edit().putInt(KEY_DAILY_STREAK, streak).apply()
    }

    fun getDailyStreak(): Int {
        return sharedPreferences.getInt(KEY_DAILY_STREAK, 0)
    }

    fun setLastLoginDate(date: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_LOGIN_DATE, date).apply()
    }

    fun getLastLoginDate(): Long {
        return sharedPreferences.getLong(KEY_LAST_LOGIN_DATE, 0)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}