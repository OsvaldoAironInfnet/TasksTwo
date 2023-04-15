package com.poc.firstprojectinfnet.home.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.tracker.TrackerLogEvent
import com.poc.commom.base.views.BaseViewModel
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.data.TaskLocate
import com.poc.firstprojectinfnet.home.data.TaskViewState
import com.poc.firstprojectinfnet.home.data.WeatherState
import com.poc.firstprojectinfnet.home.data.usecase.HomeDeleteTaskUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeRecoveryAllTasksUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeSaveTaskUseCase
import com.poc.firstprojectinfnet.home.data.usecase.HomeWeatherUseCase
import com.poc.firstprojectinfnet.profile.data.model.TasksProfile
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetDataUserUseCase
import com.poc.firstprojectinfnet.profile.data.usecase.ProfilePictureGetSettingsUseCase
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val homeDeleteTaskUseCase: HomeDeleteTaskUseCase,
    private val trackerLogEvent: TrackerLogEvent,
    private val homeSaveTaskUseCase: HomeSaveTaskUseCase,
    private val homeRecoveryAllTasksUseCase: HomeRecoveryAllTasksUseCase,
    private val profilePictureGetDataUserUseCase: ProfilePictureGetDataUserUseCase,
    private val profilePictureSettings: ProfilePictureGetSettingsUseCase,
    private val homeWeatherUseCase: HomeWeatherUseCase,
) : BaseViewModel<HomeState, HomeAction>() {

    private var homeView: HomeContract.View? = null
    private var dataReceivedLogin: Bundle? = null
    var imageUriProfile: Uri? = null

    private val dataSet: ArrayList<Task> = ArrayList<Task>()

    private val dateuser: GoogleLoginSingInDTO?
        get() {
            return try {
                var dataUser: GoogleLoginSingInDTO? = null
                profilePictureGetDataUserUseCase.getDataUser(onSuccess = { data ->
                    dataUser = data
                }, onFailure = {
                    dataUser = dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO
                })
                return dataUser
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    fun init(view: HomeContract.View, data: Bundle?) {
        homeView = view
        dataReceivedLogin = data
        homeSaveTaskUseCase.saveUserPrefs(dataReceivedLogin?.get("dataLogin") as GoogleLoginSingInDTO)
    }

    companion object {
        const val REQUEST_PERMISSION_LOCATION = 4004
    }


    fun requestWeatherData(lat: Double, long: Double) {
        viewModelScope.launch {
            homeWeatherUseCase.getWeatherData(TaskLocate(lat, long), onResult = { weatherData ->
                launch {
                    homeWeatherUseCase.getDistrictData(
                        TaskLocate(lat, long),
                        onResult = { districtDate ->
                            state.value =
                                HomeState(weatherState = WeatherState(districtDate, weatherData))
                        },
                        onFailure = {
                            Log.d("Weather", "Failure to obtain District Data")
                        })
                }
            }, onFailure = {
                Log.d("Weather", "Failure to obtain Weather Data")
            })
        }
    }

    fun openALocationManager() {
        action.value = HomeAction.OpenLocationManager
    }

    fun givePermissionLocation() {
        Log.d("Location", "Give permission Location")
        action.value = HomeAction.GivePermissionLocation
    }

    fun trackOpenNavigation() {
        trackerLogEvent.trackEventClick(
            event = "abrir_navegacao",
            category = "NAVEGACAO_HOME",
            eventLabel = "Abrir Navegacao"
        )
    }

    fun trackLoadProfileSettingsNavigation() {
        trackerLogEvent.trackEventClick(
            event = "abrir_navegacao",
            category = "NAVEGACAO_HOME",
            eventLabel = "Carregar perfil"
        )
    }

    fun trackCloseNavigation() {
        trackerLogEvent.trackEventClick(
            event = "fechar_navegacao",
            category = "NAVEGACAO_HOME",
            eventLabel = "Fechar Navegacao"
        )
    }

    fun recoveryProfileImage() {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetImageProfile(onRecoveryImage = { uri ->
                imageUriProfile = uri
                action.value = HomeAction.SetupProfileNavigationImage.apply { image = uri }
            }, onFailure = {}, userId = it.id)
        }, onFailure = {})
    }

    fun recoveryProfilePicture(onResult: (Uri) -> Unit, onFailure: () -> Unit) {
        profilePictureGetDataUserUseCase.getDataUser(onSuccess = {
            profilePictureSettings.onGetImageProfile(onRecoveryImage = { uri ->
                onResult.invoke(uri)
            }, onFailure = {
                onFailure.invoke()
            }, userId = it.id)
        }, onFailure = {
            onFailure.invoke()
        })
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
            homeSaveTaskUseCase.saveLocalTask(it)
            dataSet.clear()
            dataSet.addAll(it)
            state.value = HomeState(TaskViewState(dataSetRecoveryLocal = dataSet))
        }, onFailure = {})
    }

    fun createTask(
        title: String,
        description: String,
        isFav: Boolean,
        date: String,
        hour: String,
        crit: String
    ) {
        val task = Task(
            title,
            description,
            favorite = isFav,
            id = dateuser?.id!!,
            date = date,
            hour = hour,
            crit = crit,
            validationId = UUID.randomUUID().toString()
        )
        homeSaveTaskUseCase.saveRemoteTask(task)
        val calendarScheduleTask1 = createScheduleNotification(task)
        action.value = HomeAction.RedirectToHomeWithArgs.apply {
            message = "Tarefa criada com sucesso!"
            calendarScheduleTask = calendarScheduleTask1
            this.titleTask = task.title
        }
    }

    fun listAllFavoriteTasksSelected() {
        val dataSetFavorite = ArrayList<Task>()
        dataSet.forEach {
            if (it.favorite) {
                dataSetFavorite.add(it)
            }
        }
        viewModelScope.launch {

        }
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
                homeDeleteTaskUseCase.deleteTask(it, onSuccess = {
                    dataSet.clear()
                }, onFailure = {})
            }
        }
    }

    fun redirectToDetailPage(task: Task?, viewParam: View) {
        action.value = HomeAction.RedirectToDetail.apply {
            detailTask = task
            view = viewParam
        }
    }

    fun redirectToAddTaskPage() {
        action.value = HomeAction.RedirectToAddTaskPage
    }


    fun recoveryFavoriteTasks() {
        homeRecoveryAllTasksUseCase.recoveryAllRemoteTasks(onRecoveryAllData = {
            val dt = ArrayList<Task>()
            it.forEach { task ->
                if (task.favorite) {
                    dt.add(task)
                }
            }
            state.value = HomeState(tasksProfile = TasksProfile(favoriteDatasetTask = dt))
        }, onFailure = { _ ->
            action.value = HomeAction.GenericToastError.apply {
                message = "Não foi possivel obter as tarefas!"
            }
        })
    }

    private fun createScheduleNotification(task: Task): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(
            task.date.substring(6, 10).toInt(),
            task.date.substring(3, 5).toInt() - 1,
            task.date.substring(0, 2).toInt(),
            task.hour.substring(0, 2).toInt(),
            task.hour.substring(3, 5).toInt(),
            0
        )
        return calendar
    }

    fun onLogoutApp() {
        FirebaseAuth.getInstance().signOut()
        trackerLogEvent.trackEventClick(event = "Sair do App", "Logout", "sair_do_aplicativo")
        action.value = HomeAction.OnLogoutApp
    }
}