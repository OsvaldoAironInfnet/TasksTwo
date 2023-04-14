package com.poc.firstprojectinfnet.home.presentation


import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.ActivityHomeBinding
import com.poc.firstprojectinfnet.home.navigation.RedirectHomeFlowEnum
import com.poc.firstprojectinfnet.home.presentation.weather.WeatherFragment
import com.poc.firstprojectinfnet.login.navigation.RedirectLoginFlowEnum
import com.poc.firstprojectinfnet.profile.data.model.ProfilePicture
import com.poc.firstprojectinfnet.profile.presentation.ui.profilepicture.ProfilePictureFragment
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject
import java.text.DateFormatSymbols
import java.util.*


class HomeActivity : BaseActivity(), HomeContract.View {

    private var _binding: ActivityHomeBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    private val navigationView by lazy {
        _binding?.navView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)

        setNavigationRoot(R.id.nav_host_fragment_main)
        supportActionBar?.hide()
        setContentView(_binding?.root)

        setupControllerNavigationView()

        setupHomeViewModel()

        onClickBtnProfileAction()

        observeAction()
        observeState()

        setupDescriptionDataHome()

        onClickBtnFabAction()

        setupProfile()
        setupWeather()
        logoutApp()
    }

    override fun onResume() {
        super.onResume()
        setupProfile()
    }

    private fun logoutApp() {
        _binding?.btnLogout?.setOnClickListener {
            AlertDialog.Builder(this@HomeActivity).setTitle(
                "Sair do App"
            ).setMessage("Fazer logout do app?").setPositiveButton(
                "Sim"
            ) { _, _ ->
                homeViewModel.onLogoutApp()
            }.setNegativeButton("Não") { _, _ -> }.show()
        }
    }

    private fun setupDescriptionDataHome() {
        val calendar = Calendar.getInstance()

        val dayOfWeek = dayOfWeek(calendar)
        val nameOfMonth = nameOfMonth(calendar)

        _binding?.descriptionHome?.text = "${dayOfWeek?.capitalize()},${nameOfMonth?.capitalize()}"
    }

    private fun setupWeather() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.weather_view, WeatherFragment(), "WeatherFragment").commit()
    }

    private fun observeState() = with(homeViewModel) {
        state.observe(this@HomeActivity) { homeState ->
            setupProfileSettings(homeState.profilePictureState)
        }
    }

    private fun observeAction() = with(homeViewModel) {
        action.observe(this@HomeActivity) { homeAction ->
            when (homeAction) {
                HomeAction.SetupProfileNavigationImage -> {
                    setupProfileImageNav(HomeAction.SetupProfileNavigationImage.image)
                    trackLoadProfileSettingsNavigation()
                }
                HomeAction.RedirectToAddTaskPage -> {
                    navigationScreen?.navigate(RedirectHomeFlowEnum.HOME_ADD_FRAGMENT.navigationScreen)
                }
                HomeAction.OnLogoutApp -> {
                    finishAffinity()
                }
            }
        }
    }

    private fun setupProfileSettings(profilePicture: ProfilePicture?) {
        profilePicture?.let {
            val headerView = navigationView?.getHeaderView(0)
            val textName = headerView?.findViewById<TextView>(R.id.name)
            textName?.text = it.name

            val textEmail = headerView?.findViewById<TextView>(R.id.email)
            textEmail?.text = it.email
        }
    }

    private fun setupProfileImageNav(image: Uri?) {
        val headerView = navigationView?.getHeaderView(0)
        val imageView = headerView?.findViewById<CircleImageView>(R.id.imageView)
        imageView?.let {
            Glide
                .with(this@HomeActivity)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(it)
        }

    }

    private fun setupProfile() {
        homeViewModel.recoveryProfileImage()
        homeViewModel.recoveryProfileSettings()
    }

    private fun onClickBtnProfileAction() {
        _binding?.btnProfile?.setOnClickListener {
            configureShowVisibileAnimationNavigationView()
            homeViewModel.trackOpenNavigation()
        }
    }

    private fun setupHomeViewModel() = with(homeViewModel) {
        init(this@HomeActivity, intent.getBundleExtra("responseData"))
        recoveryAllTasks()
    }

    private fun onClickBtnFabAction() {
        _binding?.fab?.setOnClickListener {
            homeViewModel.redirectToAddTaskPage()
        }
    }

    private fun setupControllerNavigationView() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_main)
        navigationView?.setupWithNavController(navController)
    }

    private fun configureShowVisibileAnimationNavigationView() {
        val transition: Transition = Slide(Gravity.LEFT)
        transition.duration = 400
        transition.addTarget(navigationView)

        TransitionManager.beginDelayedTransition(_binding!!.root, transition)
        navigationView?.visibility = View.VISIBLE
    }

    private fun configureHideVisibileAnimationNavigationView() {
        val transition: Transition = Slide(Gravity.LEFT)
        transition.duration = 400
        transition.addTarget(navigationView)

        TransitionManager.beginDelayedTransition(_binding!!.root, transition)
        navigationView?.visibility = View.GONE
    }

    private fun dayOfWeek(calendar: Calendar): String? {
        return DateFormatSymbols().weekdays[calendar[Calendar.DAY_OF_WEEK]]
    }

    private fun nameOfMonth(calendar: Calendar): String? {
        return DateFormatSymbols().months[calendar[Calendar.MONTH]]
    }

    override fun showMessage(string: String) {}

    override fun redirectToLogin() {}

    override fun redirectToListTask() {
        navigationScreen?.navigate(RedirectHomeFlowEnum.HOME_LIST_FRAGMENT.navigationScreen)
    }

    private fun showToolbar() {
        _binding?.toolbar?.visibility = View.VISIBLE
    }

    private fun showFloactingButton() {
        _binding?.fab?.visibility = View.VISIBLE
    }

    fun hideToolbar() {
        _binding?.toolbar?.visibility = View.GONE
    }

    fun hideFloactingButton() {
        _binding?.fab?.visibility = View.GONE
    }

    fun hideNavigationView() {
        configureHideVisibileAnimationNavigationView()
    }

    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main)

        when (fragment?.childFragmentManager?.fragments?.first()) {
            is ProfilePictureFragment -> {
                (fragment.childFragmentManager.fragments.first() as ProfilePictureFragment).let { it.hideRootView() }
                showToolbar()
                showFloactingButton()
                homeViewModel.trackCloseNavigation()
            }
            is HomeAddTaskFragment -> {
                showToolbar()
                showFloactingButton()
            }
            is HomeTaskDetailFragment -> {

            }
            else -> Unit
        }

        if (_binding?.navView?.isVisible == true) {
            configureHideVisibileAnimationNavigationView()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}