package com.poc.storage

interface StorageDAO<T> {
    var referenceDatabaseName: String

    fun saveData(data: T, userId: String? = null)

    fun removeData(data: T? = null, onSuccess: () -> Unit, onFailure: () -> Unit)

    fun saveListData(data: List<T>, userId: String? = null)

    fun getOneData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit,
        userId: String? = null
    )

    fun recoveryAllData(onRecovery: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit)

    fun clearAllData(onSuccess: () -> Unit, onFailure: () -> Unit)
}