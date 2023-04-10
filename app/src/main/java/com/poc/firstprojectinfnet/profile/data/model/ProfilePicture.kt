package com.poc.firstprojectinfnet.profile.data.model

import android.graphics.Bitmap
import android.net.Uri

data class ProfilePicture(
    val id: String? = null,
    val name: String,
    val imageUri: Uri? = null,
    val imageBitmap: Bitmap? = null,
    val email: String? = null
) {
}