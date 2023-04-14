package com.poc.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

interface GoogleLoginProvider {
    fun onSign(activity: AppCompatActivity, requestCode: Int)
    fun getDataIntentSignIn(
        data: Intent, onSuccess: (GoogleLoginSingInDTO) -> Unit,
        onFailure: () -> Unit
    )
}