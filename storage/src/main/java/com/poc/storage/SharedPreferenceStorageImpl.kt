package com.poc.storage

import android.content.Context

class SharedPreferenceStorageImpl<T>(override var referenceDatabaseName: String, context: Context) :
    StorageDAO<T> {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            referenceDatabaseName,
            Context.MODE_PRIVATE
        )
    }

    override fun saveData(data: T) {
        sharedPreferences.edit().apply {
            putString("userData", data.toString())
        }.apply()
    }

    override fun getOneData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit,userId: String?) {
        val recoveryDataUser = sharedPreferences.getString("userData", "")

        if (recoveryDataUser != "") {
            val map = HashMap<String, String>()
            map["userData"] = recoveryDataUser.toString()
            onRecovery.invoke(map)
        } else {
            onFailure.invoke("Don't have data user in local memory")
        }
    }


    override fun recoveryAllData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {}


}