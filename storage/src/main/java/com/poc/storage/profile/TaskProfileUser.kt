package com.poc.storage.profile

import android.graphics.Bitmap
import android.net.Uri

data class TaskProfileUser(val id: String, val imageUri: Uri?, val imageBitmap: Bitmap?) {
}