package com.poc.firstprojectinfnet.profile.data.usecase

import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.data.repository.ProfilePictureRepository

class ProfilePictureSaveUseCase(private val profilePictureRepository: ProfilePictureRepository) {
    fun saveProfile(profilePicture: ProfilePicture) {
        profilePictureRepository.saveProfile(profilePicture)
    }
}