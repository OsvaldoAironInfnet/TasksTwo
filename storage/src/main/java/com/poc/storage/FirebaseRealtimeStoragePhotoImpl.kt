package com.poc.storage

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poc.storage.profile.TaskProfileUser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FirebaseRealtimeStoragePhotoImpl<T : Any>(override var referenceDatabaseName: String) :
    StorageDAO<T> {

    private val databaseStorage by lazy {
        FirebaseStorage.getInstance().reference
    }

    private val refDatabaseStoragePhoto = databaseStorage.child(referenceDatabaseName)


    inline fun <reified T : Any> Any.cast(): T {
        return this as T
    }

    override fun saveData(data: T) {
        val id = data::class.java.getDeclaredField("id")
        id.isAccessible = true
        val idString = id.get(data)

        try {
            val imageUri = data::class.java.getDeclaredField("imageUri")
            imageUri.isAccessible = true
            val imageUriValue = imageUri.get(data) as Uri
            imageUriValue.let {
                if (it != null) {
                    refDatabaseStoragePhoto.child("User-$idString").putFile(it)
                }
            }
        } catch (e: Exception) {
            try {
                val imageBitMap = data::class.java.getDeclaredField("imageBitmap")
                imageBitMap.isAccessible = true
                val imageBitMapValue = imageBitMap.get(data) as Bitmap


                imageBitMapValue.let { bitmap ->
                    if (bitmap != null) {
                        val tempFile = File.createTempFile("temprentpk", ".png")
                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
                        val bitmapData = bytes.toByteArray()

                        val fileOutPut = FileOutputStream(tempFile)
                        fileOutPut.write(bitmapData)
                        fileOutPut.flush()
                        fileOutPut.close()
                        val uri = Uri.fromFile(tempFile)

                        refDatabaseStoragePhoto.child("User-$idString").putFile(uri)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getOneData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit,
        userId: String?
    ) {
        refDatabaseStoragePhoto.child("User-$userId").downloadUrl
            .addOnSuccessListener {
                val map = HashMap<String, Uri>()
                map["photoProfile"] = it
                onRecovery.invoke(map)
            }.addOnFailureListener {
                onFailure.invoke("Failure to obtain photo profile.")
            }
    }

    override fun recoveryAllData(
        onRecovery: (Map<String, Any>) -> Unit,
        onFailure: (String) -> Unit
    ) {
    }
}