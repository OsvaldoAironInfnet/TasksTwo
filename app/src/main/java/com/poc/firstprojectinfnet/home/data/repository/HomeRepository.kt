package com.poc.firstprojectinfnet.home.data.repository

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.data.BaseRepository
import com.poc.commom.base.data.repository.StorageRepository
import com.poc.commom.base.safeLet
import com.poc.firstprojectinfnet.home.data.DistrictDate
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.TaskLocate
import com.poc.firstprojectinfnet.home.data.WeatherData
import com.poc.firstprojectinfnet.home.data.datasource.DistrictDataSource
import com.poc.firstprojectinfnet.home.data.datasource.WeatherDataSource

class HomeRepository(
    private val storageRepository: StorageRepository<Task>,
    private val userRepository: StorageRepository<GoogleLoginSingInDTO>,
    private val districtDataSource: DistrictDataSource,
    private val weatherDataSource: WeatherDataSource
) :
    BaseRepository<Task>() {

    fun saveUserPrefs(data: GoogleLoginSingInDTO) {
        userRepository.saveUserPrefs(data)
    }

    fun saveARemoteTask(task: Task) {
        storageRepository.saveDataRealtimeDatabase(task)
        Log.d("Task", "Save Realtime task")
    }

    fun saveALocalTask(task: List<Task>, userId: String?) {
        storageRepository.saveDataTaskLocal(task, userId)
    }

    fun deleteTask(task: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        storageRepository.deleteTask(task, onSuccess, onFailure)
    }

    fun getAllRemoteTask(onRecoveryAllData: (List<Task>) -> Unit, onFailure: (String) -> Unit) {
        storageRepository.getAllRecoveryDataRealTimeDatabase(onRecovery = {
            val dataSet = ArrayList<Task>()
            it.forEach { data ->
                val taskRecovery: Task = gson.fromJson(gson.toJson(data.value), object : TypeToken<Task>() {}.type)
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


    suspend fun getDataDistrictCity(
        taskLocate: TaskLocate,
        onResult: (DistrictDate) -> Unit,
        onFailure: () -> Unit
    ) {
        safeLet(taskLocate.lat, taskLocate.long, result = { lat, long ->
            onResult.invoke(districtDataSource.getCityDistrict(lat, long))
        }, failure = {
            onFailure.invoke()
        })
    }

    suspend fun getWeatherData(
        taskLocate: TaskLocate,
        onResult: (WeatherData) -> Unit,
        onFailure: () -> Unit
    ) {
        safeLet(taskLocate.lat, taskLocate.long, result = { lat, long ->
            onResult.invoke(weatherDataSource.getWeatherData(lat, long))
        }, failure = {
            onFailure.invoke()
        })
    }
}