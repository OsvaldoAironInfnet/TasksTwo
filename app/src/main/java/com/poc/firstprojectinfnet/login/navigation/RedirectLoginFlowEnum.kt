package com.poc.firstprojectinfnet.login.navigation

import com.poc.commom.base.views.navigation.NavigationScreen
import com.poc.firstprojectinfnet.login.presentation.LoginFragment
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.login.presentation.SplashScreenFragment

enum class RedirectLoginFlowEnum(val navigationScreen: NavigationScreen) {

    SPLASH_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.splashFragment,
            SplashScreenFragment::class.java.name
        )
    ),
    LOGIN_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.loginFragment,
            LoginFragment::class.java.name
        )
    )
}