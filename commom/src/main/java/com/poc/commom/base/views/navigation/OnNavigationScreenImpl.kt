package com.poc.commom.base.views.navigation

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController

class OnNavigationScreenImpl(private val navController: NavController?) : OnNavigationScreen {

    override fun navigate(navData: NavigationScreen) {
        Log.d("OnNavigationScreen", "Navigate to: " + navData.name)
        navController?.navigate(navData.id)
    }

    override fun navigate(navData: NavigationScreen, args: Bundle) {

    }

    override fun navigate(navData: NavigationScreen, onResult: () -> Unit) {

    }


}