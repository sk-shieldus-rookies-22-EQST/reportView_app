package com.example.reportview_003.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "UserSession"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_TOKEN = "user_token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginSession(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TOKEN, token)
        editor.apply()
    }

    fun getUserID(context: Context):String? {
        return getPreferences(context).getString(PREF_NAME, null)
    }

    fun getUserToken(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_TOKEN, null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession(context: Context) {
        val editor = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}