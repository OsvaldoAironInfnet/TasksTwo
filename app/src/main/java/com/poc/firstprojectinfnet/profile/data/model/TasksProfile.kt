package com.poc.firstprojectinfnet.profile.data.model

import com.poc.firstprojectinfnet.home.data.Task

data class TasksProfile(
    var favoriteTasks: Int? = null,
    var allTasks: Int? = null,
    var favoriteDatasetTask: List<Task>? = null
) {
}