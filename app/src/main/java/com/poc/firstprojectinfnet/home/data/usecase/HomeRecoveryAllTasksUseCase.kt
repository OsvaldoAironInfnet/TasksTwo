package com.poc.firstprojectinfnet.home.data.usecase

import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.repository.HomeRepository

class HomeRecoveryAllTasksUseCase(private val homeRepository: HomeRepository) {
    fun recoveryAllRemoteTasks(
        onRecoveryAllData: (List<Task>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        homeRepository.getAllRemoteTask(onRecoveryAllData, onFailure)
    }
}