package com.poc.firstprojectinfnet.login.presentation

import com.poc.commom.base.views.UiAction

open class LoginAction : UiAction {
    object RedirectToForgotPassword : LoginAction()
    object GenericToastError : LoginAction() {
        var message: String? = null
    }
}