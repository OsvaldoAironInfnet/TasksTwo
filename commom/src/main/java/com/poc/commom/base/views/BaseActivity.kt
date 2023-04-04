package com.poc.commom.base.views

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.poc.commom.base.views.navigation.OnNavigationScreen
import com.poc.commom.base.views.navigation.OnNavigationScreenImpl

open class BaseActivity : AppCompatActivity() {

    private var navHostFragment: NavHostFragment? = null
    var navigationScreen: OnNavigationScreen? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun setNavigationRoot(navHostFragmentLayoutId: Int) {
        navHostFragment = supportFragmentManager.findFragmentById(navHostFragmentLayoutId) as NavHostFragment
        navigationScreen = OnNavigationScreenImpl(navController = navHostFragment?.navController)
    }

    fun validateUsername(string: String) =
        string.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(string).matches()
}