package com.poc.commom.base.views.navigation

import android.os.Bundle

data class NavigationScreen(
    val id: Int, val name: String, val displayName: String? = null,
    var bundle: Bundle? = null
)