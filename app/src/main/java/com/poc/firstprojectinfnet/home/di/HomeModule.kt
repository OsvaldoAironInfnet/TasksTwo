package com.poc.firstprojectinfnet.home.di

import com.poc.firstprojectinfnet.home.data.datasource.DistrictDataSource
import com.poc.firstprojectinfnet.home.data.datasource.WeatherDataSource
import com.poc.firstprojectinfnet.home.data.repository.HomeRepository
import com.poc.firstprojectinfnet.home.data.usecase.HomeDeleteTaskUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeRecoveryAllTasksUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeSaveTaskUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeWeatherUseCase
import com.poc.firstprojectinfnet.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {

    val homeModule = module {
        factory { WeatherDataSource() }
        factory { DistrictDataSource() }

        single {
            HomeRepository(get(), get(), get(), get())
        }

        single {
            HomeSaveTaskUseCase(get())
        }

        single {
            HomeWeatherUseCase(get())
        }

        single {
            HomeRecoveryAllTasksUseCase(get())
        }

        single {
            HomeDeleteTaskUseCase(get())
        }

        single {
            HomeViewModel(get(), get(), get(), get(), get(), get(), get())
        }

    }
}