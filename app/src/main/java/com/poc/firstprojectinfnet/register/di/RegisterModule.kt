package com.poc.firstprojectinfnet.register.di

import com.poc.firstprojectinfnet.register.presentation.RegisterViewModel
import org.koin.dsl.module

object RegisterModule {

    val registerModule = module {
        factory<RegisterViewModel> {
            RegisterViewModel(get())
        }
    }
}