package com.poc.firstprojectinfnet.home.data.usecase

import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.repository.HomeRepository

class HomeDeleteTaskUseCase(private val homeRepository: HomeRepository) {
    fun deleteTask(task: Task, onSuccess: () -> Unit, onFailure: () -> Unit) {
        homeRepository.deleteTask(task, onSuccess, onFailure)
    }
}