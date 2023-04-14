package com.poc.firstprojectinfnet.home.navigation

import com.poc.commom.base.views.navigation.NavigationScreen
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.home.presentation.HomeAddTaskFragment
import com.poc.firstprojectinfnet.home.presentation.HomeListFragment
import com.poc.firstprojectinfnet.home.presentation.HomeTaskDetailFragment

enum class RedirectHomeFlowEnum(val navigationScreen: NavigationScreen) {
    HOME_LIST_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.homeListFragment, HomeListFragment::class.java.name
        )
    ),
    HOME_ADD_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.homeAddFragment, HomeAddTaskFragment::class.java.name
        )
    ),
    HOME_DETAIL_FRAGMENT(
        navigationScreen = NavigationScreen(
            R.id.homeDetailFragment,
            HomeTaskDetailFragment::class.java.name
        )
    )
}