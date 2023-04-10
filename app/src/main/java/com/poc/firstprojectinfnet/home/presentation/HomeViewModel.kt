package com.poc.firstprojectinfnet.home.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.tracker.TrackerLogEvent
import com.poc.commom.base.views.BaseViewModel
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.TaskViewState
import com.poc.firstprojectinfnet.home.data.usecase.HomeRecoveryAllTasksUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeSaveTaskUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetDataUserUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetSettingsUseCase
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureAction
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedPreferences: SharedPreferences,
    private val trackerLogEvent: TrackerLogEvent,
    private val homeSaveTaskUseCase: HomeSaveTaskUseCase,
    private val homeRecoveryAllTasksUseCase: HomeRecoveryAllTasksUseCase,
    private val profilePictureGetDataUserUseCase: ProfilePictureGetDataUserUseCase,
    private val profilePictureSettings: ProfilePictureGetSettingsUseCase
) : BaseViewModel<HomeState, HomeAction>() {

    private var homeView: HomeContract.View? = null
    private var dataReceivedLogin: Bundle? = null

    private val _viewState: MutableLiveData<TaskViewState> = MutableLiveData(TaskViewState())
    val viewState: LiveData<TaskViewState> = _viewState
    private val dataSet: ArrayList<Task> = ArrayList<Task>()

    fun init(view: HomeContract.View, data: Bundle?) {
        homeView = view
        dataReceivedLogin = data
        homeSaveTaskUseCase.saveUserPrefs(dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO)
    }


    fun trackOpenNavigation() {
        trackerLogEvent.trackEventClick(event = "abrir_navegacao", category = "NAVEGACAO_HOME", eventLabel = "Abrir Navegacao")
    }

    fun trackLoadProfileSettingsNavigation() {
        trackerLogEvent.trackEventClick(event = "abrir_navegacao", category = "NAVEGACAO_HOME", eventLabel = "Carregar perfil")
    }

    fun trackCloseNavigation() {
        trackerLogEvent.trackEventClick(event = "fechar_navegacao", category = "NAVEGACAO_HOME", eventLabel = "Fechar Navegacao")
    }

    fun recoveryProfileImage() {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetImageProfile(onRecoveryImage = {
                action.value = HomeAction.SetupProfileNavigationImage.apply { image = it }
            }, onFailure = {}, userId = it.id)
        }, onFailure = {})
    }

    fun recoveryProfileSettings() {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetSettingsProfile(onRecovery = { profilePicture ->
                state.value = HomeState(profilePictureState = profilePicture)
            }, onFailure = {

            }, it.id)
        }, onFailure = {})
    }

    fun recoveryAllTasks() {
        homeRecoveryAllTasksUseCase.recoveryAllRemoteTasks(onRecoveryAllData = {

        }, onFailure = {

        })


        val dataUser = dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO
        val result = sharedPreferences.getString(dataUser.id, "")
        try {
            val dataRecovery: ArrayList<Task> =
                Gson().fromJson(result, object : TypeToken<ArrayList<Task?>?>() {}.type)
            dataSet.addAll(dataRecovery)
            val emitter = TaskViewState(dataSetRecoveryLocal = dataRecovery)
            state.value = HomeState(TaskViewState(dataSetRecoveryLocal = dataRecovery))
            _viewState.value = emitter
        } catch (e: Exception) {
            Log.d("fail recovery", "fail recovery data")
        }
    }

    fun createTask(title: String, description: String, isFav: Boolean?) {
        val dataUser = dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO
        viewModelScope.launch {
            isFav?.let {
                val task = Task(
                    title,
                    description,
                    isFavorite = it,
                    id = dataUser.id!!
                )
                dataSet.add(task)
                _viewState.value = TaskViewState(dataSetRecoveryLocal = dataSet)
                saveLocalATask()
            }
                ?: also {
                    val task = Task(
                        title,
                        description,
                        isFavorite = false,
                        id = dataUser.id!!
                    )

                    dataSet.add(task)
                    _viewState.value = TaskViewState(dataSetRecoveryLocal = dataSet)
                    saveLocalATask()
                }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveLocalATask() {
        val dataUser = dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO
        sharedPreferences.edit()?.apply {
            putString(dataUser.id, Gson().toJson(dataSet)).apply()
        }
    }

    fun listAllFavoriteTasksSelected() {
        val dataSetFavorite = ArrayList<Task>()
        dataSet.forEach {
            if (it.isFavorite) {
                dataSetFavorite.add(it)
            }
        }
        viewModelScope.launch {
            _viewState.value =
                TaskViewState(dataSetRecoveryLocal = dataSetFavorite, listFavorite = true)
        }
    }

    fun listAllTasks() {
        _viewState.postValue(TaskViewState(dataSetRecoveryLocal = dataSet))
    }

    fun deleteItem(task: Task?) {
        var searchedTask: Task? = null
        task?.let { deleteTask ->
            dataSet.forEach {
                if (deleteTask.title == it.title && deleteTask.description == it.description) {
                    searchedTask = it
                }
            }

            searchedTask?.let {
                dataSet.remove(it)
                saveLocalATask()
            }
        }
    }
}