package com.poc.commom.base.data.repository

import android.net.Uri
import com.poc.storage.FirebaseRealtimeStoragePhotoImpl
import com.poc.storage.StorageDAO as FirebaseStorageDao
import com.poc.storage.StorageDAO as FirebaseRealTimeProfile
import com.poc.storage.StorageDAO as SharedPreferenceStorage
import com.poc.storage.StorageDAO as SharedPreferenceTaskStorage

class StorageRepository<T : Any>(
    private val firebaseRealtimeStorage: FirebaseStorageDao<T>,
    private val sharedPreferenceStorage: SharedPreferenceStorage<T>,
    private val sharedPreferenceTaskStorage: SharedPreferenceTaskStorage<T>,
    private val firebaseStoragePhoto: FirebaseRealtimeStoragePhotoImpl<Any>,
    private val firebaseRealtimeProfile: FirebaseRealTimeProfile<T>
) {

    fun saveUserPrefs(data: T) {
        sharedPreferenceStorage.saveData(data)
    }

    fun getUserPrefs(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        sharedPreferenceStorage.getOneData(onRecovery, onFailure)
    }

    fun saveDataRealtimeDatabase(data: T) {
        firebaseRealtimeStorage.saveData(data = data)
    }

    fun saveDataTaskLocal(data: List<T>, userId: String?) {
        sharedPreferenceTaskStorage.saveListData(data,userId)
    }

    fun deleteTask(data: T, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseRealtimeStorage.removeData(data,onSuccess, onFailure)
    }

    fun recoveryDataRealtimeDatabase(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseRealtimeStorage.getOneData(onRecovery, onFailure)
    }

    fun getAllRecoveryDataRealTimeDatabase(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) =
        firebaseRealtimeStorage.recoveryAllData(onRecovery, onFailure)

    fun saveStoragePhoto(data: T) {
        firebaseStoragePhoto.saveData(data)
    }

    fun saveProfile(data: T) {
        firebaseRealtimeProfile.saveData(data)
    }

    fun recoveryProfileSettings(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseRealtimeProfile.recoveryAllData(onRecovery, onFailure)
    }

    fun recoveryProfileSettings(
        onRecovery: (Map<String, Uri>) -> Unit,
        onFailure: (String) -> Unit,
        userId: String? = null
    ) {
        firebaseStoragePhoto.getOneData(onRecovery = {
            onRecovery.invoke(it as Map<String, Uri>)
        }, onFailure, userId)
    }
}