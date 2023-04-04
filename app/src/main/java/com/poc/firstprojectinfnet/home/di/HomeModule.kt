package com.poc.firstprojectinfnet.home.di

import com.poc.firstprojectinfnet.home.presentation.HomeViewModel
import org.koin.dsl.module

object HomeModule {

    val homeModule = module {
        single<HomeViewModel> {
            HomeViewModel(get())
        }
    }
}