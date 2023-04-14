package com.poc.firstprojectinfnet.login.presentation

import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.views.BaseContractView

interface LoginContract {

    interface View : BaseContractView {
        fun redirectToHome(data: GoogleLoginSingInDTO)
    }
}