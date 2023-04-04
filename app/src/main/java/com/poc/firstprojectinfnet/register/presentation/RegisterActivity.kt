package com.poc.firstprojectinfnet.register.presentation


import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.ActivityRegisterBinding
import com.poc.firstprojectinfnet.databinding.FragmentLoginBinding
import com.poc.firstprojectinfnet.register.data.ValidatorPasswordEnum
import org.koin.android.ext.android.inject


class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val registerViewModel: RegisterViewModel by inject()

    private var _binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
        registerViewModel.init(this)
        createUser()
    }


    private fun createUser() {
        _binding?.btnLogin?.setOnClickListener {
            validateCredentials(onSuccess = { username, password ->
                registerViewModel.createUser(username, password)
            })
        }
    }


    private fun validateCredentials(onSuccess: (String, String) -> Unit) {
        val resultValidateUsername =
            validateUsername(_binding?.edtUsername?.editText?.text.toString())

        validatePassword(
            _binding?.edtPassword?.editText?.text.toString(),
            _binding?.edtPasswordConfirm?.editText?.text.toString(),
            onSuccess = {
                if (resultValidateUsername) {
                    onSuccess.invoke(
                        _binding?.edtUsername?.editText?.text.toString(),
                        _binding?.edtPassword?.editText?.text.toString()
                    )
                }
            },
            onFailure = {
                when (it) {
                    ValidatorPasswordEnum.PASSWORD_DIVERGENCE -> {
                        Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show()
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