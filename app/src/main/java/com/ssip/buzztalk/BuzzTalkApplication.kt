package com.ssip.buzztalk

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.ssip.buzztalk.utils.TokenManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BuzzTalkApplication: Application() {

    val TAG = "BuzzTalk"

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
                Log.d(TAG, "onCreate: ${task.result}")
                tokenManager.saveNotificationTokenOnCreate(task.result)
            }
        }
    }
}