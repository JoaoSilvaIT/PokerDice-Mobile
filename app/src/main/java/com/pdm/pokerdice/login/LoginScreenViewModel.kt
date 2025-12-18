package com.pdm.pokerdice.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.user.AuthInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserCredentials
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.login.utilis.LoginUseCase
import com.pdm.pokerdice.login.utilis.performLogin
import com.pdm.pokerdice.service.UserAuthService
import kotlinx.coroutines.launch

interface LoginScreenState {
    object Idle : LoginScreenState
    data class LoginInProgress(val tentativeCredentials: UserCredentials) : LoginScreenState
    data class LoginSuccess(val authInfo: AuthInfo) : LoginScreenState
    data class LoginError(val errorMessage: String) : LoginScreenState
}

class LoginScreenViewModel(
    private val loginUseCase: LoginUseCase,
    private val service : UserAuthService,
    private val authRepo : AuthInfoRepo
) : ViewModel() {

    companion object {
        fun getFactory(service: UserAuthService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
                    LoginScreenViewModel(
                        ::performLogin,
                        service,
                        repo
                    ) as T
                } else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    var currentState: LoginScreenState by mutableStateOf(LoginScreenState.Idle)

    fun login(credentials : UserCredentials) {
        if (currentState is LoginScreenState.LoginInProgress || currentState is LoginScreenState.LoginSuccess) {
            return
        }

        viewModelScope.launch {
            currentState = LoginScreenState.LoginInProgress(credentials)
            currentState = when (val result = loginUseCase(credentials, service, authRepo)) {
                is Either.Failure -> {
                    LoginScreenState.LoginError("Login failed: ${result.value}")
                }
                is Either.Success -> {
                    LoginScreenState.LoginSuccess(result.value)
                }
            }
        }
    }
}
