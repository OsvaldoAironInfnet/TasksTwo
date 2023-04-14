package com.poc.firstprojectinfnet.profile.presentation.ui

import com.poc.commom.base.views.UiState
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.data.model.TasksProfile

class ProfilePictureState(
    val profilePictureState: ProfilePicture? = null,
    val tasksProfile: TasksProfile? = null
) : UiState {
}