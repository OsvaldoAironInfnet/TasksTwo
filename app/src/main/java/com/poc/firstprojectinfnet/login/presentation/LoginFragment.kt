package com.poc.firstprojectinfnet.login.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.poc.commom.base.auth.GoogleLoginSingInDTO
import com.poc.commom.base.views.BaseActivity
import com.poc.commom.base.views.BaseFragment
import com.poc.firstprojectinfnet.databinding.FragmentLoginBinding
import com.poc.firstprojectinfnet.home.presentation.HomeActivity
import com.poc.firstprojectinfnet.login.presentation.LoginViewModel.Companion.REQUEST_CODE_LOGIN_GOOGLE
import com.poc.firstprojectinfnet.register.presentation.RegisterActivity
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(), LoginContract.View {
    private val loginViewModel: LoginViewModel by inject()

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel.init(this)

        authenticateWithGoogleLogin()
        authenticateWithFirebaseCredentialsLogin()
        redirectToRegisterScreen()
        return _binding?.root
    }

    private fun authenticateWithGoogleLogin() {
        _binding?.logingoogle?.setOnClickListener {
            loginViewModel.authenticateWithGoogle(
                activity as BaseActivity,
                REQUEST_CODE_LOGIN_GOOGLE
            )
        }
    }

    private fun authenticateWithFirebaseCredentialsLogin() {
        _binding?.btnLogin?.setOnClickListener {
            validateCredentials()
        }
    }

    private fun validateCredentials() {
        val validateUserName =
            (activity as BaseActivity).validateUsername(_binding?.edtUsername?.editText?.text.toString())
        val validatePassword = validatePassword(_binding?.edtPassword?.editText?.text.toString())

        if (!validateUserName) {
            showMessage("Email não pode ser vazio")
        }
        if (!validatePassword) {
            showMessage("Password não pode ser vazio")
        }

        if (validatePassword && validateUserName) {
            loginViewModel.authenticateLoginFirebase(
                _binding?.edtUsername?.editText?.text.toString(),
                _binding?.edtPassword?.editText?.text.toString()
            )
        }
    }

    private fun validatePassword(
        password: String
    ) = password.isNotBlank()


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