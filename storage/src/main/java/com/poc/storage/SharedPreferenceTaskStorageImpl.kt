package com.poc.storage

import android.content.Context
import com.google.gson.Gson

class SharedPreferenceTaskStorageImpl<T>(
    override var referenceDatabaseName: String,
    private val context: Context
) : StorageDAO<T> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            referenceDatabaseName,
            Context.MODE_PRIVATE
        )
    }

    override fun saveData(data: T, userId: String?) {
        sharedPreferences.edit()?.apply {
            putString(userId, Gson().toJson(data)).apply()
        }
    }

    override fun saveListData(data: List<T>, userId: String?) {
        sharedPreferences.edit()?.apply {
            putString(userId, Gson().toJson(data)).apply()
        }
    }

    override fun removeData(data: T?, onSuccess: () -> Unit, onFailure: () -> Unit) {}

    override fun getOneData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit,
        userId: String?
    ) {
        val recoveryDataTask = sharedPreferences.getString(userId, "")

        if (recoveryDataTask != "") {
            val map = HashMap<String, String>()
            map["dataTasks"] = recoveryDataTask.toString()
            onRecovery.invoke(map)
        } else {
            onFailure.invoke("Don't have data task in local memory")
        }
    }

    override fun recoveryAllData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {
    }

    override fun clearAllData(onSuccess: () -> Unit, onFailure: () -> Unit) {
        sharedPreferences.edit().clear().apply {
            onSuccess.invoke()
        }
    }
}