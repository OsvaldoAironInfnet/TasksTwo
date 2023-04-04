package com.poc.firstprojectinfnet.home.navigation

import com.poc.commom.base.views.navigation.NavigationScreen
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.home.presentation.HomeListFragment

enum class RedirectHomeFlowEnum(val navigationScreen: NavigationScreen) {
    HOME_LIST_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.homeListFragment, HomeListFragment::class.java.name
        )
    )
}