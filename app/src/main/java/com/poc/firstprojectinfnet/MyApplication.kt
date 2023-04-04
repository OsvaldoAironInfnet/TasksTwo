package com.poc.firstprojectinfnet

import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.poc.commom.base.di.CommonModule
import com.poc.firstprojectinfnet.home.di.HomeModule
import com.poc.firstprojectinfnet.login.di.LoginModule
import com.poc.firstprojectinfnet.register.di.RegisterModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger(level = Level.INFO)
            androidContext(this@MyApplication)
            modules(getModulesKoin())
        }
    }


    private fun getModulesKoin() = arrayListOf(
        globalSharedPreference(),
        CommonModule.commomModule,
        LoginModule.loginModule,
        RegisterModule.registerModule,
        HomeModule.homeModule
    )

    private fun globalSharedPreference() = module {
        single<SharedPreferences> { getSharedPreferences("TASKS_APP", MODE_PRIVATE) }
    }
}