package com.poc.firstprojectinfnet.home.di

import com.poc.firstprojectinfnet.home.data.repository.HomeRepository
import com.poc.firstprojectinfnet.home.data.usecase.HomeRecoveryAllTasksUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeSaveTaskUseCase
import com.poc.firstprojectinfnet.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {

    val homeModule = module {
        single {
            HomeRepository(get(), get())
        }

        single {
            HomeSaveTaskUseCase(get())
        }

        single {
            HomeRecoveryAllTasksUseCase(get())
        }

        single {
            HomeViewModel(get(), get(), get(), get(), get(), get())
        }

    }
}