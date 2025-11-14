package com.pdm.pokerdice.ui.authentication.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.login_signup.InvalidCredentialsException
import com.pdm.pokerdice.login_signup.LoginService
import com.pdm.pokerdice.login_signup.LoginUseCase
import com.pdm.pokerdice.login_signup.UserCredentials
import com.pdm.pokerdice.login_signup.performLogin
import com.pdm.pokerdice.ui.authentication.signUp.SignUpViewModel
import kotlinx.coroutines.launch

interface LoginState {

    object Idle : LoginState

    data class LoginInProgress(val tentativeCredentials: UserCredentials) : LoginState

    data class LoginSuccess(val token : String) : LoginState

    data class LoginError(val errorMessage: String) : LoginState
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val service : LoginService,
    private val authRepo : AuthInfoRepo
) : ViewModel() {

    companion object {
        /**
         * Returns a factory to create a [LoginViewModel] with the provided [LoginService].
         * @param service The service to be used to login.
         * @param repo The repository to manage authentication information.
         * Design challenge: Should we also pass here the use case function? Why? Why not?
         */
        fun getFactory(service: LoginService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                    LoginViewModel(
                        ::performLogin,
                        service,
                        repo
                    ) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    /**
     * The current state of the login screen.
     */
    var currentState: LoginState by mutableStateOf(LoginState.Idle)

    fun login(credentials : UserCredentials) {
        viewModelScope.launch {
            currentState = try {
                val token = loginUseCase(credentials, service, authRepo).authToken
                LoginState.LoginSuccess(token)
            } catch (e: InvalidCredentialsException) {
                LoginState.LoginError("Invalid credentials provided")
            }
        }
    }
}