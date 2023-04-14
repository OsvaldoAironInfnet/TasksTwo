package com.poc.firstprojectinfnet.login.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.views.BaseActivity
import com.poc.commom.base.views.BaseFragment
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.login.navigation.RedirectLoginFlowEnum
import org.koin.android.ext.android.inject


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment(), LoginContract.View {
    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.init(view = this@SplashScreenFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onResume() {
        super.onResume()
        loginViewModel.redirectToLoginPage()
    }


    override fun redirectToLogin() {
        (activity as BaseActivity).navigationScreen?.navigate(RedirectLoginFlowEnum.LOGIN_FRAGMENT.navigationScreen)
    }

    override fun redirectToHome(data: GoogleLoginSingInDTO) {}

    override fun showMessage(message: String) {}

}