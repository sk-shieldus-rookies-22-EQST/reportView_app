package com.example.bookies_001.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "pref_obj"
    private const val KEY_IS_LOGGED_IN = "is_logged_in" // 로그인 확인
    private const val KEY_USER_ID = "user_id"
    private const val USER_LEVEL = "user_level"
    private const val AUTO_LOGIN = "auto_login"
    private const val REMEMBER_ID = "remember_id"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginSession(context: Context, user_id: String) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        val encryptedUserID = SaveUserInfo.encrypt(user_id)
        editor.putString(KEY_USER_ID, encryptedUserID)
        editor.apply()
    }

    fun saveUserLevel(context: Context, userLevel: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(USER_LEVEL,userLevel)
        editor.apply()
    }

    fun saveAutoLogin(context: Context, autoLogin: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(AUTO_LOGIN, autoLogin)
        editor.apply()
    }

    fun saveRememberID(context: Context, rememberID: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(REMEMBER_ID, rememberID)
        editor.apply()
    }

    fun getUserID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val encryptedUserID = sharedPreferences.getString(KEY_USER_ID, null)
        return encryptedUserID?.let { SaveUserInfo.decrypt(it) }
    }

    fun getUserLevel(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(USER_LEVEL,-1)
    }

    fun isAutoLogin(context: Context): Boolean {
        return getPreferences(context).getBoolean(AUTO_LOGIN, false)
    }

    fun isRememberID(context: Context): Boolean {
        return getPreferences(context).getBoolean(REMEMBER_ID, false)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearSession(context: Context) {
        val editor = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit()

        val autoLogin = isAutoLogin(context)
        val rememberID = isRememberID(context)
        val userID = getUserID(context)
        val userLevel = getUserLevel(context)

        editor.remove(KEY_IS_LOGGED_IN)

        editor.putBoolean(AUTO_LOGIN, autoLogin)
        editor.putBoolean(REMEMBER_ID, rememberID)

        if (!rememberID) {
            editor.remove(KEY_USER_ID)
            editor.remove(USER_LEVEL)
        } else {
            val encryptedUserID = userID?.let { SaveUserInfo.encrypt(it) }
            editor.putString(KEY_USER_ID, encryptedUserID)
            editor.putInt(USER_LEVEL,userLevel)
        }

        editor.apply()
    }
}