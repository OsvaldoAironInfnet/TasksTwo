package com.poc.firstprojectinfnet.profile.data.usecase

import android.net.Uri
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.data.repository.ProfilePictureRepository

class ProfilePictureGetSettingsUseCase(private val profilePictureRepository: ProfilePictureRepository) {
    fun onGetImageProfile(
        onRecoveryImage: (Uri) -> Unit,
        onFailure: (String) -> Unit,
        userId: String? = null
    ) {
        profilePictureRepository.getSettingsProfile(onRecoveryImage, onFailure, userId)
    }

    fun onGetSettingsProfile(
        onRecovery: (ProfilePicture) -> Unit,
        onFailure: (String) -> Unit,
        userId: String? = null
    ) {
        profilePictureRepository.getSettingsProfileData(onRecovery, onFailure, userId)

    }
}