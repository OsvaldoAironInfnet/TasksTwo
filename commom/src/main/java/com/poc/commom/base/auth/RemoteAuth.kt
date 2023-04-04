package com.poc.commom.base.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.poc.commom.base.views.BaseActivity
import com.poc.firebaseauth.FirebaseCredentialsProvider
import com.poc.firebaseauth.GoogleLoginProvider
import com.poc.firebaseauth.RegisterUserFirebaseEnum

class RemoteAuth(
    private val googleLoginProvider: GoogleLoginProvider,
    private val firebaseCredentialsProvider: FirebaseCredentialsProvider
) {

    fun onLoginGoogle(baseActivity: BaseActivity, requestCode: Int) {
        googleLoginProvider.onSign(baseActivity as AppCompatActivity, requestCode)
    }

    fun onGetDataIntentSignIn(
        data: Intent,
        onSuccess: (GoogleLoginSingInDTO) -> Unit,
        onFailure: () -> Unit
    ) {
        googleLoginProvider.getDataIntentSignIn(data, onSuccess = {
            onSuccess.invoke(GoogleLoginSingInDTO(it.email, it.id,"google"))
        }, onFailure)
    }

    fun onRegisterUserFirebaseCredentials(
        email: String,
        password: String,
        onFailure: (com.poc.commom.base.auth.RegisterUserFirebaseEnum) -> Unit,
        onSuccess: (String) -> Unit
    ) {

        firebaseCredentialsProvider.onRegisterUser(
            email, password,
            onFailure = {
                when (it) {
                    RegisterUserFirebaseEnum.ERROR_UNDEFINED -> {
                        onFailure.invoke(com.poc.commom.base.auth.RegisterUserFirebaseEnum.ERROR_UNDEFINED)
                    }
                    RegisterUserFirebaseEnum.ERROR_EMAIL_ALREADY_IN_USE -> {
                        onFailure.invoke(com.poc.commom.base.auth.RegisterUserFirebaseEnum.ERROR_EMAIL_ALREADY_IN_USE)
                    }
                    RegisterUserFirebaseEnum.ERROR_WEAK_PASSWORD -> {
                        onFailure.invoke(com.poc.commom.base.auth.RegisterUserFirebaseEnum.ERROR_WEAK_PASSWORD)
                    }
                }
            }, onSuccess
        )
    }

    fun onLoginFirebaseCredentials(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseCredentialsProvider.onLoginUser(email, password, onSuccess, onFailure)
    }
}