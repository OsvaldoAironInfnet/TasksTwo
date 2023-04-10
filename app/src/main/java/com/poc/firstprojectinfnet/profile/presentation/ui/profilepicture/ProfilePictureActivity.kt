package com.poc.firstprojectinfnet.profile.presentation.ui.profilepicture

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.ActivityProfilePictureBinding
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureAction
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureViewModel
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureViewModel.Companion.REQUEST_PERMISSION_CAMERA
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureViewModel.Companion.REQUEST_PERMISSION_GALLERY
import org.koin.android.ext.android.inject

class ProfilePictureActivity : AppCompatActivity() {

    private var _binding: ActivityProfilePictureBinding? = null
    private val profilePictureViewModel: ProfilePictureViewModel by inject()

    private val openCameraResult = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { image ->
        profilePictureViewModel.sendAImageBitMap(image)
    }

    private val openGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        profilePictureViewModel.sendAImageUri(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfilePictureBinding.inflate(layoutInflater)

        setContentView(_binding!!.root)
        supportActionBar?.hide()

        profilePictureViewModel.recoveryProfileImage()

        openCameraPhoto()
        openGalleryPhoto()
        observeAction()
        saveAProfile()

    }

    private fun saveAProfile() {
        _binding?.btnSaveProfile?.setOnClickListener {
            profilePictureViewModel.validateInputProfile(_binding?.edtNameProfile?.text?.toString())
        }
    }

    private fun observeAction() = with(profilePictureViewModel) {
        action.observe(this@ProfilePictureActivity) { action ->
            when (action) {
                is ProfilePictureAction.GivePermissionCamera -> {
                    givePermissionOpenCamera()
                }
                is ProfilePictureAction.OpenCamera -> {
                    this@ProfilePictureActivity.openCamera()
                }
                is ProfilePictureAction.OpenGallery -> {
                    this@ProfilePictureActivity.openGallery()
                }
                is ProfilePictureAction.GenericToastError -> {
                    val toast = action
                    Toast.makeText(this@ProfilePictureActivity, toast.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is ProfilePictureAction.SendImageBitMapToUI -> {
                    val imageCaptured = action.image
                    _binding?.profileImageView?.setImageBitmap(imageCaptured)
                }
                is ProfilePictureAction.SendImageUriToUI -> {
                    val imageCaptured = action.image
                    _binding?.profileImageView?.setImageURI(imageCaptured)
                }

                is ProfilePictureAction.GivePermissionGallery -> {
                    this@ProfilePictureActivity.givePermissionGallery()
                }
                is ProfilePictureAction.SaveProfileSuccessful -> {
                    closeActivityAndShowMessageProfileOK()
                }

                is ProfilePictureAction.ShowProfileImage -> {
                    showProfileImage(ProfilePictureAction.ShowProfileImage.image)
                }
                else -> Unit
            }
        }
    }

    private fun showProfileImage(image: Uri?) {
        _binding?.profileImageView?.let {
            Glide
                .with(this@ProfilePictureActivity)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(it)
        }
    }

    private fun closeActivityAndShowMessageProfileOK() {
        Toast.makeText(this, "Perfil salvo com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun openGallery() {
        openGalleryResult.launch("image/*")
    }

    private fun openCamera() {
        openCameraResult.launch(null)
    }

    private fun openGalleryPhoto() {
        _binding?.imageButtonGallery?.setOnClickListener {
            profilePictureViewModel.givePermissionGallery()
        }
    }

    private fun openCameraPhoto() {
        _binding?.imageButtonCamera?.setOnClickListener {
            profilePictureViewModel.givePermissionCamera()
        }
    }


    private fun givePermissionGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_GALLERY
            )
        } else {
            profilePictureViewModel.openGallery()
        }
    }

    private fun givePermissionOpenCamera() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_PERMISSION_CAMERA
            )
        } else {
            profilePictureViewModel.openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                profilePictureViewModel.openCamera()
            } else {
                profilePictureViewModel.showErrorGivePermissionCamera()
            }
        }
    }
}