package com.poc.firstprojectinfnet.home.presentation

import com.poc.commom.base.views.UiState
import com.poc.firstprojectinfnet.home.data.TaskViewState
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture

data class HomeState(
    val taskViewState: TaskViewState? = null,
    val profilePictureState: ProfilePicture? = null
) : UiState {
}