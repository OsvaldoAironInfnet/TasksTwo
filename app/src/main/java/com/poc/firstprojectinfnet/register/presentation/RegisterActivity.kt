package com.poc.firstprojectinfnet.register.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.components.ButtonPrimaryComponentFragment
import com.poc.firstprojectinfnet.components.EmailComponentInputFragment
import com.poc.firstprojectinfnet.components.PasswordComponentInputFragment
import com.poc.firstprojectinfnet.databinding.ActivityRegisterBinding
import com.poc.firstprojectinfnet.register.data.ValidatorPasswordEnum
import org.koin.android.ext.android.inject

class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val registerViewModel: RegisterViewModel by inject()

    private var _binding: ActivityRegisterBinding? = null

    private val passwordInputFragment by lazy {
        PasswordComponentInputFragment()
    }

    private val passwordConfirmInputFragment by lazy {
        PasswordComponentInputFragment(hintPassword = getString(R.string.cpassword))
    }

    private val btnLoginFragment by lazy {
        ButtonPrimaryComponentFragment(
            onClickButton = {
                validateCredentials(onSuccess = { username, password ->
                    registerViewModel.createUser(username, password)
                })
            },
            textButton = this.getString(R.string.register)
        )
    }

    private val emailInputFragment by lazy {
        EmailComponentInputFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
        registerViewModel.init(this)
        setupComponents()
    }

    @SuppressLint("CommitTransaction")
    private fun setupComponents() {
        supportFragmentManager.beginTransaction().replace(
            R.id.edt_password,
            passwordInputFragment,
            "PASSWORD_FRAGMENT_INPUT"
        ).commit()

        supportFragmentManager.beginTransaction().replace(
            R.id.edt_password_confirm,
            passwordConfirmInputFragment,
            "PASSWORD_CONFIRM_FRAGMENT_INPUT"
        ).commit()


        supportFragmentManager.beginTransaction().replace(
            R.id.btn_login,
            btnLoginFragment,
            "BTN_SAVE_FRAGMENT"
        ).commit()

        supportFragmentManager.beginTransaction().replace(
            R.id.edt_username,
            emailInputFragment,
            "EMAIL_FRAGMENT_INPUT"
        ).commit()
    }

    private fun validateCredentials(onSuccess: (String, String) -> Unit) {
        emailInputFragment.getEmailValid()?.let { emailValid ->
            passwordInputFragment.getPasswordText()
            passwordConfirmInputFragment.getPasswordText()

            validatePassword(
                passwordInputFragment.getPasswordText(),
                passwordConfirmInputFragment.getPasswordText(),
                onSuccess = {
                    onSuccess.invoke(
                        emailValid,
                        passwordInputFragment.getPasswordText()
                    )
                },
                onFailure = {
                    when (it) {
                        ValidatorPasswordEnum.PASSWORD_DIVERGENCE -> {
                            Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        ValidatorPasswordEnum.PASSWORD_EMPTY -> {
                            Toast.makeText(this, "Senha vazia!", Toast.LENGTH_SHORT).show()
                        }
                        ValidatorPasswordEnum.CONFIRM_PASSWORD_EMPTY -> {
                            Toast.makeText(this, "Confirmação de senha vazia!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {}
                    }
                })

        } ?: also {
            showMessage("Email não pode ser vazio")
        }
    }


    private fun validatePassword(
        password: String,
        confirmPassword: String,
        onFailure: (ValidatorPasswordEnum) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (password.isNotBlank() && confirmPassword.isNotBlank() && (password == confirmPassword)) {
            onSuccess.invoke()
        } else if (password.isBlank()) {
            onFailure.invoke(ValidatorPasswordEnum.PASSWORD_EMPTY)
        } else if (confirmPassword.isBlank()) {
            onFailure.invoke(ValidatorPasswordEnum.CONFIRM_PASSWORD_EMPTY)
        } else {
            onFailure.invoke(ValidatorPasswordEnum.PASSWORD_DIVERGENCE)
        }
    }

    override fun showMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun redirectToLogin() {
        finish()
    }
}