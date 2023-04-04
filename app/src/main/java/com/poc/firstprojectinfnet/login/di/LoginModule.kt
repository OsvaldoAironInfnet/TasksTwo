package com.poc.firstprojectinfnet.login.di

import com.poc.firstprojectinfnet.login.presentation.LoginViewModel
import org.koin.dsl.module

object LoginModule {

    val loginModule = module {
        factory<LoginViewModel> {
            LoginViewModel(get())
        }
    }
}