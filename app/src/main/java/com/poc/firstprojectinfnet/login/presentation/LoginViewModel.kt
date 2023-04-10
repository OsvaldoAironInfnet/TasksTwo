package com.poc.firstprojectinfnet.login.presentation

import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.auth.RemoteAuth
import com.poc.commom.base.tracker.TrackerLogEvent
import com.poc.commom.base.views.BaseActivity
import com.poc.commom.base.views.BaseViewModel

class LoginViewModel(private val remoteAuth: RemoteAuth, private val tracker: TrackerLogEvent) :
    BaseViewModel<LoginState, LoginAction>() {

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
                redirectToHomeApp(GoogleLoginSingInDTO(email, it, "firebase"))
            }, 1000)
        }, onFailure = {
            loginView?.showMessage(it)
        })
    }

    private fun trackLoginSucessful() {
        tracker.trackEventClick("login_ok", "LOGIN", "Login realizado com sucesso")
    }

    private fun redirectToHomeApp(data: GoogleLoginSingInDTO) {
        loginView?.redirectToHome(data)
        trackLoginSucessful()
    }

    fun redirectToForgotPassword() {
        action.value = LoginAction.RedirectToForgotPassword
    }

    fun onResetPassword(email: String) {
        remoteAuth.onResetPassword(email, onSuccess = {
            action.value = LoginAction.GenericToastError.apply {
                message = "Verifique sua caixa de e-mails"
            }
        }, onFailure = {
            action.value = LoginAction.GenericToastError.apply {
                message = "Não foi possivel enviar um e-mail de redefinição, tente novamente!"
            }
        })
    }

    companion object {
        private const val DELAY_TIME_SLEEP_SPLASH_SCREEN = 3000L
        const val REQUEST_CODE_LOGIN_GOOGLE = 3004
    }
}