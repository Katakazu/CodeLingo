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
        private const val KEY_CURRENT_LESSON = "current_lesson"
        private const val KEY_TOTAL_XP = "total_xp"
        private val KEY_TODAY_XP = "today_xp"
        private val KEY_TODAY_XP_DATE = "today_xp_date"
        private val KEY_CLAIMED_QUESTS = "claimed_quests"
        private val KEY_LESSON_TODAY = "lesson_today"
        private val KEY_LESSON_TODAY_DATE = "lesson_today_date"
    }

    init {
        resetDailyQuestsIfNeeded()
    }

    private fun resetDailyQuestsIfNeeded() {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_TODAY_XP_DATE, null)
        if (today != savedDate) {
            resetTodayXp()
            resetClaimedQuests()
            resetLessonsCompletedToday()
            // Simpan tanggal baru
            sharedPreferences.edit().putString(KEY_TODAY_XP_DATE, today).apply()
        }
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

    fun setCurrentLesson(lesson: Int) {
        sharedPreferences.edit().putInt(KEY_CURRENT_LESSON, lesson).apply()
    }
    fun getCurrentLesson(): Int {
        return sharedPreferences.getInt(KEY_CURRENT_LESSON, 1)
    }

    fun setTotalXp(totalXp: Int) {
        sharedPreferences.edit().putInt(KEY_TOTAL_XP, totalXp).apply()
    }
    fun getTotalXp(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_XP, 0)
    }

    // XP harian
    fun getTodayXp(): Int {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_TODAY_XP_DATE, null)
        return if (today == savedDate) {
            sharedPreferences.getInt(KEY_TODAY_XP, 0)
        } else {
            0
        }
    }
    fun addTodayXp(xp: Int) {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_TODAY_XP_DATE, null)
        val currentXp = if (today == savedDate) sharedPreferences.getInt(KEY_TODAY_XP, 0) else 0
        sharedPreferences.edit()
            .putInt(KEY_TODAY_XP, currentXp + xp)
            .putString(KEY_TODAY_XP_DATE, today)
            .apply()
    }
    fun resetTodayXp() {
        sharedPreferences.edit().putInt(KEY_TODAY_XP, 0).putString(KEY_TODAY_XP_DATE, getTodayString()).apply()
    }
    private fun getTodayString(): String {
        val now = java.util.Calendar.getInstance()
        return "%04d-%02d-%02d".format(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH) + 1, now.get(java.util.Calendar.DAY_OF_MONTH))
    }

    // Claimed Quests
    fun getClaimedQuests(): Set<String> {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_TODAY_XP_DATE, null)
        return if (today == savedDate) {
            sharedPreferences.getStringSet(KEY_CLAIMED_QUESTS, emptySet()) ?: emptySet()
        } else {
            emptySet()
        }
    }
    fun addClaimedQuest(questId: String) {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_TODAY_XP_DATE, null)
        val set = if (today == savedDate) sharedPreferences.getStringSet(KEY_CLAIMED_QUESTS, mutableSetOf())?.toMutableSet() ?: mutableSetOf() else mutableSetOf()
        set.add(questId)
        sharedPreferences.edit()
            .putStringSet(KEY_CLAIMED_QUESTS, set)
            .putString(KEY_TODAY_XP_DATE, today)
            .apply()
    }
    fun resetClaimedQuests() {
        sharedPreferences.edit().putStringSet(KEY_CLAIMED_QUESTS, emptySet()).apply()
    }

    // Tambah XP ke total dan harian
    fun addXp(xp: Int) {
        setTotalXp(getTotalXp() + xp)
        setUserXp(getUserXp() + xp)
        addTodayXp(xp)
    }

    fun getLessonsCompletedToday(): Int {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_LESSON_TODAY_DATE, null)
        return if (today == savedDate) {
            sharedPreferences.getInt(KEY_LESSON_TODAY, 0)
        } else {
            0
        }
    }
    fun addLessonCompletedToday() {
        val today = getTodayString()
        val savedDate = sharedPreferences.getString(KEY_LESSON_TODAY_DATE, null)
        val current = if (today == savedDate) sharedPreferences.getInt(KEY_LESSON_TODAY, 0) else 0
        sharedPreferences.edit()
            .putInt(KEY_LESSON_TODAY, current + 1)
            .putString(KEY_LESSON_TODAY_DATE, today)
            .apply()
    }
    fun resetLessonsCompletedToday() {
        sharedPreferences.edit().putInt(KEY_LESSON_TODAY, 0).putString(KEY_LESSON_TODAY_DATE, getTodayString()).apply()
    }
}