package com.poc.firstprojectinfnet.home.data

data class TaskViewState(
    var dataSetRecoveryLocal: ArrayList<Task>? = null,
    var dataSetChangedExecution: Task? = null,
    var listFavorite: Boolean = false
) {
}