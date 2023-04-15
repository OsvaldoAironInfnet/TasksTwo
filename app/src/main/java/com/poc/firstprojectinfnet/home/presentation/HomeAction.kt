package com.poc.firstprojectinfnet.home.presentation

import android.net.Uri
import android.view.View
import com.poc.commom.base.views.UiAction
import com.poc.firstprojectinfnet.home.data.Task
import java.util.*

open class HomeAction : UiAction {

    object GenericToastError : HomeAction() {
        var message: String? = null
    }

    object SetupProfileNavigationImage : HomeAction() {
        var image: Uri? = null
    }

    object GivePermissionLocation : HomeAction()
    object OpenLocationManager : HomeAction()

    object RedirectToAddTaskPage : HomeAction()

    object RedirectToHomeWithArgs : HomeAction() {
        var message: String? = null
        var calendarScheduleTask: Calendar? = null
        var titleTask: String = ""
    }

    object RedirectToDetail : HomeAction() {
        var detailTask: Task? = null
        var view: View? = null
    }

    object OnLogoutApp : HomeAction()
}