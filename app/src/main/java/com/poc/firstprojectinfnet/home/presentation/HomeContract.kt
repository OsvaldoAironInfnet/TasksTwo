package com.poc.firstprojectinfnet.home.presentation


import com.poc.commom.base.views.BaseContractView

interface HomeContract {
    interface View: BaseContractView {
        fun redirectToListTask()
    }
}