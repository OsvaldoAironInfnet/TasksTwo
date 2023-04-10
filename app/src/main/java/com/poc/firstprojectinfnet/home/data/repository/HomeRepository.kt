package com.poc.firstprojectinfnet.home.data.repository

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.data.BaseRepository
import com.poc.commom.base.data.repository.StorageRepository
import com.poc.firstprojectinfnet.home.data.Task

class HomeRepository(
    private val storageRepository: StorageRepository<Task>,
    private val userRepository: StorageRepository<GoogleLoginSingInDTO>
) :
    BaseRepository<Task>() {

    fun saveUserPrefs(data: GoogleLoginSingInDTO) {
        userRepository.saveUserPrefs(data)
    }

    fun saveARemoteTask(task: Task) {
        storageRepository.saveDataRealtimeDatabase(task)
        Log.d("Task", "Save Realtime task")
    }


    fun getAllRemoteTask(onRecoveryAllData: (List<Task>) -> Unit, onFailure: (String) -> Unit) {
        val dataSet = ArrayList<Task>()
        storageRepository.getAllRecoveryDataRealTimeDatabase(onRecovery = {
            it.forEach { data ->
                val taskRecovery: Task =
                    gson.fromJson(gson.toJson(data.value), object : TypeToken<Task>() {}.type)
                dataSet.add(taskRecovery)
            }
            Log.d("Task", "Recovery All Realtime Tasks")
            onRecoveryAllData.invoke(dataSet)
        }, onFailure)
    }

    fun getOneRemoteTask(onRecoveryData: (Task) -> Unit, onFailure: (String) -> Unit) {
        storageRepository.recoveryDataRealtimeDatabase(onRecovery = {
            it.forEach { data ->
                val taskRecovery: Task =
                    gson.fromJson(gson.toJson(data.value), object : TypeToken<Task>() {}.type)
                onRecoveryData.invoke(taskRecovery)
            }
            Log.d("Task", "Recovery One Realtime Task")
        }, onFailure)
    }

}