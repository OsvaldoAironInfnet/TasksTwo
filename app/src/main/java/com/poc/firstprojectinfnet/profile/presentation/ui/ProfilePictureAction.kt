package com.poc.firstprojectinfnet.profile.presentation.ui

import android.graphics.Bitmap
import android.net.Uri
import com.poc.commom.base.views.UiAction

open class ProfilePictureAction : UiAction {
    object StartProfileActivity : ProfilePictureAction()
    object GivePermissionCamera : ProfilePictureAction()
    object GivePermissionGallery : ProfilePictureAction()
    object OpenCamera : ProfilePictureAction()
    object OpenGallery : ProfilePictureAction()
    object GenericToastError : ProfilePictureAction() {
        var message: String? = null
    }

    object SendImageBitMapToUI : ProfilePictureAction() {
        var image: Bitmap? = null
    }

    object SendImageUriToUI : ProfilePictureAction() {
        var image: Uri? = null
    }

    object SaveProfileSuccessful : ProfilePictureAction()
    object ShowProfileImage: ProfilePictureAction() {
        var image: Uri? = null
    }
}