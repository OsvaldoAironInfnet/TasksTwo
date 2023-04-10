package com.poc.commom.base.di

import com.poc.commom.base.auth.RemoteAuth
import com.poc.commom.base.data.repository.StorageRepository
import com.poc.commom.base.tracker.TrackerLogEvent
import com.poc.commom.base.tracker.TrackerLogEventImpl
import com.poc.firebaseauth.FirebaseCredentialsProvider
import com.poc.firebaseauth.GoogleLoginProvider
import com.poc.firebaseauth.NetworkProviderAuthenticator
import com.poc.storage.FirebaseRealtimeStorageImpl
import com.poc.storage.FirebaseRealtimeStoragePhotoImpl
import com.poc.storage.SharedPreferenceStorageImpl
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

        single {
            StorageRepository<Any>(
                firebaseRealtimeStorage = FirebaseRealtimeStorageImpl("tasks"),
                sharedPreferenceStorage = SharedPreferenceStorageImpl(
                    "tasks_prefs",
                    androidContext()
                ),
                firebaseStoragePhoto = FirebaseRealtimeStoragePhotoImpl("tasks_profile_photo"),
                firebaseRealtimeProfile = FirebaseRealtimeStorageImpl("profile")
            )
        }

        factory {
            TrackerLogEventImpl() as TrackerLogEvent
        }
    }
}