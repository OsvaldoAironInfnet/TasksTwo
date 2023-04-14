package com.poc.firstprojectinfnet.login

import android.os.Handler
import com.poc.commom.base.auth.RemoteAuth
import com.poc.firstprojectinfnet.login.presentation.LoginContract
import com.poc.firstprojectinfnet.login.presentation.LoginViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer


class LoginViewModelTest {


    private val remoteAuthMock = mockk<RemoteAuth>()
    private val mockContractLoginView = spyk<LoginContract.View>()
    private val loginViewModel = spyk(LoginViewModel(remoteAuthMock))

    private val handlerMockSpy = mock(Handler::class.java)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `should setter contract view in init viewModel method`() {
        loginViewModel.init(mockContractLoginView)
        assertEquals(mockContractLoginView, loginViewModel.loginView)
    }

    @Test
    fun `when loginContractView not empty should call redirectToLoginPage`() {

        // given
        loginViewModel.init(mockContractLoginView)

        // when
        loginViewModel.handler = handlerMockSpy
        loginViewModel.redirectToLogin()

        //then
        verify {
            loginViewModel.init(any())
            loginViewModel.handler = any()
            loginViewModel.redirectToLogin()
        }
    }

}