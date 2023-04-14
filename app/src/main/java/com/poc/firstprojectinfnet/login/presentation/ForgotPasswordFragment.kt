package com.poc.firstprojectinfnet.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.components.ButtonPrimaryComponentFragment
import com.poc.firstprojectinfnet.components.EmailComponentInputFragment
import com.poc.firstprojectinfnet.databinding.FragmentForgotPasswordBinding
import com.poc.firstprojectinfnet.login.navigation.RedirectLoginFlowEnum
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : Fragment(), OnLoginBackPressed {

    private var binding: FragmentForgotPasswordBinding? = null
    private val loginViewModel: LoginViewModel by inject()

    private val btnForgotPasswordFragment by lazy {
        ButtonPrimaryComponentFragment(
            onClickButton = {
                sendEmailConfirmAction()
            },
            textButton = requireContext().getString(R.string.forgot_password_btn)
        )
    }

    private val emailInputFragment by lazy {
        EmailComponentInputFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater)

        observeAction()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
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

    private fun showMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailConfirmAction() {
        emailInputFragment.getEmailValid()?.let {
            loginViewModel.onResetPassword(it)
        } ?: also {
            Toast.makeText(requireContext(), "Digite um e-mail válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun close() {
        activity?.onBackPressed()
    }

    private fun setupComponents() {
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.btn_forgot_password,
            btnForgotPasswordFragment,
            "BTN_FORGOT_PASSWORD_FRAGMENT"
        )?.commit()

        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.edt_username,
            emailInputFragment,
            "FORGOT_EMAIL_FRAGMENT_INPUT"
        )?.commit()
    }

    override fun onLoginBackPressed() = true
}