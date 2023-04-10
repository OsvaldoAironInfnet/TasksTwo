package com.poc.firstprojectinfnet.login.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.poc.commom.base.auth.GoogleLoginSingInDTO

import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.home.presentation.HomeActivity
import com.poc.firstprojectinfnet.login.navigation.RedirectLoginFlowEnum
import com.poc.firstprojectinfnet.profile.presentation.ui.profilepicture.ProfilePictureFragment
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity(), LoginContract.View {

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setNavigationRoot(R.id.nav_host_fragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginViewModel.init(this)
        loginViewModel.proccessSingInGoogle(LoginViewModel.REQUEST_CODE_LOGIN_GOOGLE, data)

    }

    override fun redirectToLogin() {}

    override fun redirectToHome(data: GoogleLoginSingInDTO) {
        val intent = Intent(this, HomeActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("dataLogin", data)
        intent.putExtra("responseData", bundle)
        startActivity(intent)
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            when (navHostFragment?.findNavController()?.currentDestination?.displayName) {
                RedirectLoginFlowEnum.FORGOT_PASSWORD_FRAGMENT.navigationScreen.displayName -> {
                    navigationScreen?.navigate(RedirectLoginFlowEnum.LOGIN_FRAGMENT.navigationScreen)
                }
            }
        } else {
            super.onBackPressed()
        }
    }
}