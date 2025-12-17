package com.pdm.pokerdice.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.login.utilis.LoginUseCase
import com.pdm.pokerdice.domain.user.UserCredentials
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.login.utilis.performLogin
import com.pdm.pokerdice.service.UserAuthService
import kotlinx.coroutines.launch

interface LoginState {

    object Idle : LoginState

    data class LoginInProgress(val tentativeCredentials: UserCredentials) : LoginState

    data class LoginSuccess(val token : String) : LoginState

    data class LoginError(val errorMessage: String) : LoginState
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val service : UserAuthService,
    private val authRepo : AuthInfoRepo
) : ViewModel() {

    companion object {
        /**
         * Returns a factory to create a [LoginViewModel] with the provided [UserAuthService].
         * @param service The service to be used to login.
         * @param repo The repository to manage authentication information.
         * Design challenge: Should we also pass here the use case function? Why? Why not?
         */
        fun getFactory(service: UserAuthService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    LoginViewModel(
                        ::performLogin,
                        service,
                        repo
                    ) as T
                } else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    /**
     * The current state of the login screen.
     */
    var currentState: LoginState by mutableStateOf(LoginState.Idle)

    fun login(credentials : UserCredentials) {
        viewModelScope.launch {
            currentState = LoginState.LoginInProgress(credentials)
            currentState = when (val result = loginUseCase(credentials, service, authRepo)) {
                is Either.Failure -> {
                    LoginState.LoginError("Login failed: ${result.value}")
                }
                is Either.Success -> {
                    LoginState.LoginSuccess(result.value.authToken)
                }
            }
        }
    }
}