package com.poc.firstprojectinfnet.profile.data.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.firstprojectinfnet.profile.data.repository.ProfilePictureRepository

class ProfilePictureGetDataUserUseCase(private val profilePictureRepository: ProfilePictureRepository) {
    fun getDataUser(onSuccess: (GoogleLoginSingInDTO) -> Unit, onFailure: (String) -> Unit) {
        profilePictureRepository.getDataUser(onSuccess = {
            try {
                val result = it.split("=")
                val email = result[1].split(",")[0]
                val id = result[2].split(",")[0]
                onSuccess.invoke(GoogleLoginSingInDTO(email, id, ""))
            } catch (e: Exception) {
                onFailure
            }
        }, onFailure)
    }
}