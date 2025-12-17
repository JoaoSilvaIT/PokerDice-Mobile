package com.pdm.pokerdice.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.AuthInfoRepo
import com.pdm.pokerdice.login.InvalidCredentialsException
import com.pdm.pokerdice.domain.user.NewUserCredentials
import com.pdm.pokerdice.signUp.utilis.SignUpUseCase
import com.pdm.pokerdice.signUp.utilis.performSignUp
import kotlinx.coroutines.launch

interface SignUpState {
    object Idle : SignUpState
    data class SignUpInProgress(val tentativeCredentials: NewUserCredentials) : SignUpState
    data class SignUpSuccess(val token : String) : SignUpState
    data class SignUpError(val errorMessage: String) : SignUpState
}

class SignUpViewModel(
    private val signUpUseCase : SignUpUseCase,
    private val service : SignUpService,
    private val authRepo : AuthInfoRepo
) : ViewModel() {
    companion object {
        /**
         * Returns a factory to create a [SignUpViewModel] with the provided [SignUpService].
         * @param service The service to be used to login.
         * @param repo The repository to manage authentication information.
         * Design challenge: Should we also pass here the use case function? Why? Why not?
         */
        fun getFactory(service: SignUpService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                    SignUpViewModel(
                        ::performSignUp,
                        service,
                        repo
                    ) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    /**
     * The current state of the sign up screen.
     */
    var currentState: SignUpState by mutableStateOf(SignUpState.Idle)

    /**
     * Initiates the signUp process with the provided [credentials].
     * @param credentials The user credentials to be used for login.
     */
    fun signUp(credentials : NewUserCredentials) {
        viewModelScope.launch {
            currentState = try {
                val token = signUpUseCase(credentials, service, authRepo).authToken
                SignUpState.SignUpSuccess(token)
            } catch (e: InvalidCredentialsException) {
                SignUpState.SignUpError(errorMessage = e.message ?: "Authentication error")
            }
        }
    }
}