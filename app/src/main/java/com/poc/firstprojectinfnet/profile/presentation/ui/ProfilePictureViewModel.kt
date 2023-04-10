package com.poc.firstprojectinfnet.profile.presentation.ui

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.safeLet
import com.poc.commom.base.views.BaseViewModel
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetDataUserUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetSettingsUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureSaveUseCase

class ProfilePictureViewModel(
    private val profilePictureSaveUseCase: ProfilePictureSaveUseCase,
    private val profilePictureGetDataUserUseCase: ProfilePictureGetDataUserUseCase,
    private val profilePictureSettings: ProfilePictureGetSettingsUseCase
) :
    BaseViewModel<ProfilePictureState, ProfilePictureAction>() {

    var imageBitMap: Bitmap? = null
    var imageUri: Uri? = null


    fun recoveryProfileImage() {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetImageProfile(onRecoveryImage = {
                action.value = ProfilePictureAction.ShowProfileImage.apply { image = it }
            }, onFailure = {}, userId = it.id)
        }, onFailure = {})
    }

    fun recoveryProfileSettings() {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetSettingsProfile(onRecovery = { profilePicture ->
                state.value = ProfilePictureState(profilePictureState = profilePicture)
            }, onFailure = {

            }, it.id)
        }, onFailure = {})
    }

    fun validateInputProfile(nameEdt: String?) {
        nameEdt?.let { nameEdt ->
            if (!nameEdt.isNullOrBlank()) {
                safeLet(imageUri, imageBitMap, { imageUri ->
                    saveProfile(imageUri, nameEdt)
                }, resultBitmap = { imageBitMap ->
                    saveProfile(imageBitMap, nameEdt)
                }, failure = {
                    val toast = ProfilePictureAction.GenericToastError
                    toast.message = "Por favor selecione, uma imagem!"
                    action.value = toast
                })
            } else {
                val toast = ProfilePictureAction.GenericToastError
                toast.message = "Campo nome, não pode ser vazio!"
                action.value = toast
            }
        } ?: also {
            val toast = ProfilePictureAction.GenericToastError
            toast.message = "Campo nome, não pode ser vazio!"
            action.value = toast
        }
    }

    private fun saveProfile(image: Bitmap, name: String) {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = { userData ->
            profilePictureSaveUseCase.saveProfile(
                ProfilePicture(
                    userData.id,
                    name,
                    null,
                    image,
                    userData.email
                )
            )
            action.value = ProfilePictureAction.SaveProfileSuccessful
        }, onFailure = {
            val toast = ProfilePictureAction.GenericToastError
            toast.message = "Não foi possivel, Salvar o perfil!"
            action.value = toast
        })

    }

    private fun saveProfile(image: Uri, name: String) {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = { userData ->
            profilePictureSaveUseCase.saveProfile(
                ProfilePicture(
                    userData.id,
                    name,
                    image,
                    null,
                    userData.email
                )
            )
            action.value = ProfilePictureAction.SaveProfileSuccessful
        }, onFailure = {
            val toast = ProfilePictureAction.GenericToastError
            toast.message = "Não foi possivel, Salvar o perfil!"
            action.value = toast
        })
    }

    fun sendAImageBitMap(image: Bitmap?) {
        imageBitMap = image
        validateImageBitMap()
    }

    fun sendAImageUri(image: Uri?) {
        imageUri = image
        validateImageUri()
    }

    private fun validateImageUri() {
        imageUri?.let {
            val imageCapture = ProfilePictureAction.SendImageUriToUI
            imageCapture.image = it
            action.value = imageCapture
        } ?: also {
            val toast = ProfilePictureAction.GenericToastError
            toast.message = "Não foi possivel, obter a imagem. Tente novamente"
            action.value = toast
        }
    }

    private fun validateImageBitMap() {
        imageBitMap?.let {
            val imageCapture = ProfilePictureAction.SendImageBitMapToUI
            imageCapture.image = it
            action.value = imageCapture
        } ?: also {
            val toast = ProfilePictureAction.GenericToastError
            toast.message = "Não foi possivel, obter a imagem. Tente novamente"
            action.value = toast
        }
    }

    fun startAProfileActivity() {
        action.value = ProfilePictureAction.StartProfileActivity
    }

    fun givePermissionGallery() {
        Log.d("GALLERY", "GIVE PERMISSION GALLERY CALL")
        action.value = ProfilePictureAction.GivePermissionGallery
    }

    fun givePermissionCamera() {
        Log.d("CAMERA", "GIVE PERMISSION CAMERA CALL")
        action.value = ProfilePictureAction.GivePermissionCamera
    }

    fun openCamera() {
        Log.d("CAMERA", "OPEN CAMERA CALL")
        action.value = ProfilePictureAction.OpenCamera
    }

    fun openGallery() {
        Log.d("GALLERY", "OPEN GALLERY CALL")
        action.value = ProfilePictureAction.OpenGallery
    }

    fun showErrorGivePermissionCamera() {
        Log.d("CAMERA", "Error Obtain Permission Camera")
        val toast = ProfilePictureAction.GenericToastError
        toast.message = "Não foi possivel, obter permissão de camera"
        action.value = toast
    }

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 3032
        const val REQUEST_PERMISSION_GALLERY = 3033
    }
}