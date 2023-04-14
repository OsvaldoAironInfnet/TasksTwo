package com.poc.firstprojectinfnet.home.data.usecase

import com.poc.firstprojectinfnet.home.data.DistrictDate
import com.poc.firstprojectinfnet.home.data.TaskLocate
import com.poc.firstprojectinfnet.home.data.WeatherData
import com.poc.firstprojectinfnet.home.data.repository.HomeRepository

class HomeWeatherUseCase(private val homeRepository: HomeRepository) {

    suspend fun getDistrictData(
        taskLocate: TaskLocate,
        onResult: (DistrictDate) -> Unit,
        onFailure: () -> Unit
    ) {
        homeRepository.getDataDistrictCity(taskLocate, onResult, onFailure)
    }

    suspend fun getWeatherData(
        taskLocate: TaskLocate,
        onResult: (WeatherData) -> Unit,
        onFailure: () -> Unit
    ) {
        homeRepository.getWeatherData(taskLocate, onResult, onFailure)
    }
}