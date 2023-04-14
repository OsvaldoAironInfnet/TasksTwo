package com.poc.firstprojectinfnet.register.presentation

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.poc.commom.base.auth.RegisterUserFirebaseEnum
import com.poc.commom.base.auth.RemoteAuth

class RegisterViewModel(private val remoteAuth: RemoteAuth) : ViewModel() {


    private var registerView: RegisterContract.View? = null

    fun init(view: RegisterContract.View) {
        registerView = view
    }


    fun createUser(username: String, password: String) {
        remoteAuth.onRegisterUserFirebaseCredentials(username, password, onFailure = {
            when (it) {
                RegisterUserFirebaseEnum.ERROR_UNDEFINED -> {
                    registerView?.showMessage("Não foi possivel criar a sua conta!")
                }
                RegisterUserFirebaseEnum.ERROR_EMAIL_ALREADY_IN_USE -> {
                    registerView?.showMessage("Email já esta em uso!!")
                }
                RegisterUserFirebaseEnum.ERROR_WEAK_PASSWORD -> {
                    registerView?.showMessage("Senha fraca, tente novamente!")
                }
                else -> {}
            }
        }, onSuccess = {
            registerView?.showMessage(it)
            Handler().postDelayed({
                registerView?.redirectToLogin()
            }, 1000)
        })
    }
}