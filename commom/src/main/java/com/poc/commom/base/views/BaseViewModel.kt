package com.poc.commom.base.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<State : UiState, Action : UiAction> : ViewModel() {
    private val _state = MutableLiveData<State>()

    private val _action = MutableLiveData<Action>()

    var state = _state

    var action = _action
}