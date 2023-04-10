package com.poc.firstprojectinfnet.profile.di

import com.poc.firstprojectinfnet.profile.data.repository.ProfilePictureRepository
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetDataUserUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetSettingsUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureSaveUseCase
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ProfilePictureModule {

    val profilePictureDep = module {

        single {
            ProfilePictureRepository(get())
        }

        single {
            ProfilePictureSaveUseCase(get())
        }
        single {
            ProfilePictureGetDataUserUseCase(get())
        }

        single {
            ProfilePictureGetSettingsUseCase(get())
        }

        viewModel {
            ProfilePictureViewModel(get(), get(), get())
        }
    }
}