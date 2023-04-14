package com.poc.firstprojectinfnet.home.presentation

import com.poc.commom.base.views.UiState
import com.poc.firstprojectinfnet.home.data.TaskViewState
import com.poc.firstprojectinfnet.home.data.WeatherState
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.data.model.TasksProfile

data class HomeState(
    val taskViewState: TaskViewState? = null,
    val profilePictureState: ProfilePicture? = null,
    val weatherState: WeatherState? = null,
    val tasksProfile: TasksProfile? = null
) : UiState {
}