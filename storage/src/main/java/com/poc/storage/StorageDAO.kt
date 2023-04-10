package com.poc.storage

interface StorageDAO<T> {
    var referenceDatabaseName: String

    fun saveData(data: T)

    fun getOneData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit, userId: String? = null)

    fun recoveryAllData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit)
}