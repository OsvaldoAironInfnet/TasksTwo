package com.poc.firstprojectinfnet.home.presentation

import android.net.Uri
import android.view.View
import com.poc.commom.base.views.UiAction
import com.poc.firstprojectinfnet.home.data.Task

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

    object RedirectToHome : HomeAction() {
        var message: String? = null
    }

    object RedirectToDetail : HomeAction() {
        var detailTask: Task? = null
        var view: View? = null
    }

    object OnLogoutApp : HomeAction()
}