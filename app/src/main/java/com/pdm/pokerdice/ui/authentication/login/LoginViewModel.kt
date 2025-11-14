package com.pdm.pokerdice.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.login_signup.LoginService
import com.pdm.pokerdice.login_signup.LoginUseCase
import com.pdm.pokerdice.login_signup.SignUpService
import com.pdm.pokerdice.login_signup.UserCredentials
import com.pdm.pokerdice.login_signup.performLogin
import com.pdm.pokerdice.login_signup.performSignUp
import com.pdm.pokerdice.ui.authentication.signUp.SignUpViewModel

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
}