package com.poc.commom.base

import android.graphics.Bitmap
import android.net.Uri
import android.util.Patterns


fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.safeLet(
    param1: String? = null,
    param2: String? = null,
    result: (String, String) -> Unit,
    failure: () -> Unit
) {
    if (param1 != null && param2 != null) {
        result.invoke(param1, param2)
    } else {
        failure.invoke()
    }
}


fun safeLet(
    param1: String? = null,
    param2: String? = null,
    param3: String? = null,
    param4: String? = null,
    result: (String, String, String, String) -> Unit,
    failure: () -> Unit
) {
    if (!param1.isNullOrBlank() && !param2.isNullOrBlank() && !param3.isNullOrBlank() && !param4.isNullOrBlank()) {
        result.invoke(param1, param2, param3, param4)
    } else {
        failure.invoke()
    }
}

suspend fun safeLet(
    param1: Double? = null,
    param2: Double? = null,
    result: suspend (Double, Double) -> Unit,
    failure: () -> Unit
) {
    if (param1 != null && param2 != null) {
        result.invoke(param1, param2)
    } else {
        failure.invoke()
    }
}

fun safeLet(
    param1: Uri? = null,
    param2: Bitmap? = null,
    resultUri: (Uri) -> Unit,
    resultBitmap: (Bitmap) -> Unit,
    failure: () -> Unit
) {
    if (param1 != null) {
        resultUri.invoke(param1)
    } else if (param2 != null) {
        resultBitmap.invoke(param2)
    } else {
        failure.invoke()
    }
}