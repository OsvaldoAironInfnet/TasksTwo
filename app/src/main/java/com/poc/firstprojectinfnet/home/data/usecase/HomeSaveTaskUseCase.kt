package com.poc.firstprojectinfnet.home.data.usecase

import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.repository.HomeRepository

class HomeSaveTaskUseCase(private val homeRepository: HomeRepository) {
    fun saveRemoteTask(data: Task) {
        homeRepository.saveARemoteTask(data)
    }

    fun saveLocalTask(data: Task) {

    }

    fun saveUserPrefs(data: GoogleLoginSingInDTO) {
        homeRepository.saveUserPrefs(data)
    }
}