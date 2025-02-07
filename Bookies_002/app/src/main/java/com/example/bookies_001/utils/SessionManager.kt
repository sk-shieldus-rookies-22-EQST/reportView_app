package com.example.bookies_001.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "1"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_TOKEN = "user_token"
    private const val USER_LEVEL = -1

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginSession(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserLevel(context: Context, userLevel: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt("user_level",userLevel)
        editor.apply()
    }

    fun getUserID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", "1")
    }

    fun getUserLevel(context: Context): Int? {
        return getPreferences(context).getInt("user_level",-1)
    }

    fun getUserToken(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_TOKEN, null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUserID(context: Context, userID: String) {
        val editor = getPreferences(context).edit()
        editor.putString("user_id", userID)
        editor.apply()
    }

    fun clearSession(context: Context) {
        val editor = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}