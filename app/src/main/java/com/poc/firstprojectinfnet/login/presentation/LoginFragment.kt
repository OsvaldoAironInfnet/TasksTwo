package com.poc.firstprojectinfnet.login.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.views.BaseActivity
import com.poc.commom.base.views.BaseFragment
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.components.ButtonPrimaryComponentFragment
import com.poc.firstprojectinfnet.components.EmailComponentInputFragment
import com.poc.firstprojectinfnet.components.PasswordComponentInputFragment
import com.poc.firstprojectinfnet.databinding.FragmentLoginBinding
import com.poc.firstprojectinfnet.home.presentation.HomeActivity
import com.poc.firstprojectinfnet.login.navigation.RedirectLoginFlowEnum
import com.poc.firstprojectinfnet.login.presentation.LoginViewModel.Companion.REQUEST_CODE_LOGIN_GOOGLE
import com.poc.firstprojectinfnet.register.presentation.RegisterActivity
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(), LoginContract.View {
    private val loginViewModel: LoginViewModel by inject()

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val passwordInputFragment by lazy {
        PasswordComponentInputFragment()
    }

    private val btnLoginFragment by lazy {
        ButtonPrimaryComponentFragment(
            onClickButton = {
                validateCredentials()
            },
            textButton = requireContext().getString(R.string.login)
        )
    }

    private val emailInputFragment by lazy {
        EmailComponentInputFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel.init(this)

        authenticateWithGoogleLogin()
        redirectToRegisterScreen()
        forgotPassword()
        setupComponents()
        observeAction()

        return _binding?.root
    }

    private fun observeAction() = with(loginViewModel) {
        action.observe(viewLifecycleOwner) { action ->
            when (action) {
                LoginAction.RedirectToForgotPassword -> {
                    (activity as BaseActivity).navigationScreen?.navigate(RedirectLoginFlowEnum.FORGOT_PASSWORD_FRAGMENT.navigationScreen)
                }
                LoginAction.GenericToastError -> {
                    LoginAction.GenericToastError.message?.let { showMessage(it) }
                }
            }
        }
    }

    private fun forgotPassword() {
        _binding?.textForgotPassword?.setOnClickListener {

            AlertDialog.Builder(requireContext()).setTitle(
                "Redefinição de senha"
            ).setMessage("Deseja redefinir sua senha?").setPositiveButton(
                "Sim"
            ) { _, _ ->
                loginViewModel.redirectToForgotPassword()
            }.setNegativeButton("Não") { _, _ ->

            }.show()
        }
    }

    private fun setupComponents() {
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.passwordInput,
            passwordInputFragment,
            "PASSWORD_FRAGMENT_INPUT"
        )?.commit()


        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.btn_login,
            btnLoginFragment,
            "BTN_SAVE_FRAGMENT"
        )?.commit()

        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.edt_username,
            emailInputFragment,
            "EMAIL_FRAGMENT_INPUT"
        )?.commit()
    }

    private fun authenticateWithGoogleLogin() {
        _binding?.logingoogle?.setOnClickListener {
            loginViewModel.authenticateWithGoogle(
                activity as BaseActivity,
                REQUEST_CODE_LOGIN_GOOGLE
            )
        }
    }

    private fun validatePassword(
        password: String
    ) = password.isNotBlank()

    private fun validateCredentials() {
        val password = passwordInputFragment.getPasswordText()

        if (validatePassword(password)) {
            if (password.length <= 6) {
                showMessage("Senha deve ser maior que 6 digitos")
            }
        } else {
            showMessage("Senha não pode ser vazia!")
        }


        emailInputFragment.getEmailValid()?.let {
            loginViewModel.authenticateLoginFirebase(
                it,
                password
            )
        } ?: also {
            showMessage("Email não pode ser vazio")
        }
    }

    private fun redirectToRegisterScreen() {
        _binding?.textRegisterHere?.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
    }

    override fun redirectToHome(data: GoogleLoginSingInDTO) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("dataLogin", data)
        intent.putExtra("responseData", bundle)
        activity?.startActivity(intent)
        activity?.finish()
    }

    override fun showMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    override fun redirectToLogin() {}
}