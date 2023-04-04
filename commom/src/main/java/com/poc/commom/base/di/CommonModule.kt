package com.poc.commom.base.di

import com.poc.commom.base.auth.RemoteAuth
import com.poc.firebaseauth.FirebaseCredentialsProvider
import com.poc.firebaseauth.GoogleLoginProvider
import com.poc.firebaseauth.NetworkProviderAuthenticator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object CommonModule {

    val commomModule = module {
        factory<RemoteAuth> {
            RemoteAuth(
                NetworkProviderAuthenticator(androidContext()) as GoogleLoginProvider,
                NetworkProviderAuthenticator(androidContext()) as FirebaseCredentialsProvider
            )
        }
    }
}