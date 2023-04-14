package com.poc.firstprojectinfnet.profile.data.repository

import android.net.Uri
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.data.BaseRepository
import com.poc.commom.base.data.repository.StorageRepository
import com.poc.commom.base.profile.TaskProfileUser
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture

class ProfilePictureRepository(private val storageRepository: StorageRepository<Any>) :
    BaseRepository<Any>() {

    fun getDataUser(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        storageRepository.getUserPrefs(onRecovery = {
            onSuccess.invoke(it["userData"] as String)
        }, onFailure)
    }

    fun saveProfile(profilePicture: ProfilePicture) {
        storageRepository.saveProfile(
            ProfilePicture(
                id = profilePicture.id,
                name = profilePicture.name,
                email = profilePicture.email
            )
        )
        storageRepository.saveStoragePhoto(
            TaskProfileUser(profilePicture.id, profilePicture.imageUri, profilePicture.imageBitmap)
        )
    }

    fun getSettingsProfile(
        onImageRecovery: (Uri) -> Unit,
        onFailure: (String) -> Unit,
        userId: String?
    ) {
        storageRepository.recoveryProfileSettings(onRecovery = {
            it["photoProfile"]?.let { it1 -> onImageRecovery.invoke(it1) }
        }, onFailure, userId)
    }


    fun getSettingsProfileData(
        onRecovery: (ProfilePicture) -> Unit,
        onFailure: (String) -> Unit,
        userId: String?
    ) {
        val dataSet = ArrayList<ProfilePicture>()
        storageRepository.recoveryProfileSettings(onRecovery = {
            it.forEach { data ->
                val taskRecovery: ProfilePicture =
                    gson.fromJson(
                        gson.toJson(data.value),
                        object : TypeToken<ProfilePicture>() {}.type
                    )
                dataSet.add(taskRecovery)
            }

            dataSet.forEach { profilePicture ->
                if (profilePicture.id == userId) {
                    onRecovery.invoke(profilePicture)
                    return@forEach;
                } else {
                    onFailure.invoke("Profile picture don't searched")
                }
            }
        }, onFailure)
    }

}