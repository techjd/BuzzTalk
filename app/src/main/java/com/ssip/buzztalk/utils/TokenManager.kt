package com.ssip.buzztalk.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs =
        context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(Constants.USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserId(userId: String) {
        val editor = prefs.edit()
        editor.putString(Constants.USER_ID, userId)
        editor.apply()
    }

    fun saveUserType(userType: String) {
        val editor = prefs.edit()
        editor.putString(Constants.USER_TYPE, userType)
        editor.apply()
    }

    fun getUserType(): String? {
        return prefs.getString(Constants.USER_TYPE, null)
    }

    fun saveNotificationTokenOnCreate(notificationToken: String) {
        val editor = prefs.edit()
        editor.putString(Constants.NOTIFICATION_ON_CREATE, notificationToken)
        editor.apply()
    }

    fun saveUserFirstTime(isUserFirstTime: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(Constants.IS_USER_FIRST_TIME, isUserFirstTime)
        editor.apply()
    }

    fun saveNotificationTokenOnNewToken(notificationToken: String) {
        val editor = prefs.edit()
        editor.putString(Constants.NOTIFICATION_ON_TOKEN, notificationToken)
        editor.apply()
    }

    fun getUserId(): String? {
        return prefs.getString(Constants.USER_ID, null)
    }

    fun getToken(): String? {
        return prefs.getString(Constants.USER_TOKEN, null)
    }

    fun getUserFirstTime(): Boolean {
        return prefs.getBoolean(Constants.IS_USER_FIRST_TIME, true)
    }

    fun getNotificationTokenOnCreate(): String? {
        return prefs.getString(Constants.NOTIFICATION_ON_CREATE, null)
    }

    fun getNotificationTokenOnNewToken(): String? {
        return prefs.getString(Constants.NOTIFICATION_ON_TOKEN, null)
    }

    fun getTokenWithBearer(): String? {
        return "Bearer ${prefs.getString(Constants.USER_TOKEN, null)}"
    }
}