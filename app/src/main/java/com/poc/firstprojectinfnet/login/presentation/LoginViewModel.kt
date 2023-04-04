package com.poc.firstprojectinfnet.login.presentation

import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.auth.RemoteAuth
import com.poc.commom.base.views.BaseActivity

class LoginViewModel(private val remoteAuth: RemoteAuth) :
    ViewModel() {

    var loginView: LoginContract.View? = null
    var handler: Handler? = null

    fun init(view: LoginContract.View) {
        loginView = view
        handler = Handler()
    }

    fun redirectToLoginPage() {
        handler?.postDelayed(
            Runnable {
                redirectToLogin()
            }, DELAY_TIME_SLEEP_SPLASH_SCREEN
        )
    }

    fun redirectToLogin() {
        loginView?.redirectToLogin()
    }

    fun authenticateWithGoogle(baseActivity: BaseActivity, requestCode: Int) {
        remoteAuth.onLoginGoogle(baseActivity, requestCode)
    }

    fun proccessSingInGoogle(requestCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            if (data != null) {
                remoteAuth.onGetDataIntentSignIn(data, onSuccess = {
                    loginView?.showMessage("Login realizado com sucesso!")
                    Handler().postDelayed({
                        redirectToHomeApp(it)
                    }, 300)
                }, onFailure = {
                    loginView?.showMessage("Não foi possivel realizar o login, tente novamente!")
                })
            }
        }
    }

    fun authenticateLoginFirebase(email: String, password: String) {
        remoteAuth.onLoginFirebaseCredentials(email, password, onSuccess = {
            loginView?.showMessage("Login realizado com sucesso!")
            Handler().postDelayed({
                redirectToHomeApp(GoogleLoginSingInDTO(null, it, "firebase"))
            }, 1000)
        }, onFailure = {
            loginView?.showMessage(it)
        })
    }


    private fun redirectToHomeApp(data: GoogleLoginSingInDTO) {
        loginView?.redirectToHome(data)
    }

    companion object {
        private const val DELAY_TIME_SLEEP_SPLASH_SCREEN = 3000L
        const val REQUEST_CODE_LOGIN_GOOGLE = 3004
    }
}