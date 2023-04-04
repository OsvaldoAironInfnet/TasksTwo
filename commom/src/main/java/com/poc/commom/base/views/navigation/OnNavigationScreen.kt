package com.poc.commom.base.views.navigation

import android.os.Bundle

interface OnNavigationScreen {
    fun navigate(navData: NavigationScreen)
    fun navigate(navData: NavigationScreen, args: Bundle)
    fun navigate(navData: NavigationScreen, onResult: () -> Unit)
}