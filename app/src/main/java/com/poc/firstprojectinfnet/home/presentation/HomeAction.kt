package com.poc.firstprojectinfnet.home.presentation

import android.net.Uri
import com.poc.commom.base.views.UiAction

open class HomeAction : UiAction {
    object SetupProfileNavigationImage: HomeAction() {
        var image: Uri? = null
    }
}