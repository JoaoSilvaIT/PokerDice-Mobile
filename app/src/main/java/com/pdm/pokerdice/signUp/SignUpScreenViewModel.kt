package com.pdm.pokerdice.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.NewUserCredentials
import com.pdm.pokerdice.domain.user.UserInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.UserAuthService
import com.pdm.pokerdice.signUp.utilis.SignUpUseCase
import com.pdm.pokerdice.signUp.utilis.performSignUp
import kotlinx.coroutines.launch

interface SignUpScreenState {
    object Idle : SignUpScreenState

    data class SignUpInProgress(
        val tentativeCredentials: NewUserCredentials,
    ) : SignUpScreenState

    data class SignUpSuccess(
        val userInfo: UserInfo,
    ) : SignUpScreenState

    data class SignUpError(
        val errorMessage: String,
    ) : SignUpScreenState
}

class SignUpScreenViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val service: UserAuthService,
    private val authRepo: AuthInfoRepo,
) : ViewModel() {
    companion object {
        fun getFactory(
            service: UserAuthService,
            repo: AuthInfoRepo,
        ) = viewModelFactory {
            initializer {
                SignUpScreenViewModel(
                    ::performSignUp,
                    service,
                    repo,
                )
            }
        }
    }

    var currentState: SignUpScreenState by mutableStateOf(SignUpScreenState.Idle)
        private set

    fun signUp(credentials: NewUserCredentials) {
        if (currentState is SignUpScreenState.SignUpInProgress || currentState is SignUpScreenState.SignUpSuccess) {
            return
        }

        viewModelScope.launch {
            currentState = SignUpScreenState.SignUpInProgress(credentials)
            when (val result = signUpUseCase(credentials, service, authRepo)) {
                is Either.Success -> {
                    val authInfo = result.value
                    currentState = SignUpScreenState.SignUpSuccess(UserInfo(authInfo.userId, authInfo.authToken))
                }
                is Either.Failure -> {
                    currentState = SignUpScreenState.SignUpError(errorMessage = "Sign up failed: ${result.value}")
                }
            }
        }
    }
}
