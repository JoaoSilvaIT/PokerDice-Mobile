package com.pdm.pokerdice.signUp.utilis

import com.pdm.pokerdice.domain.user.AuthInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.NewUserCredentials
import com.pdm.pokerdice.domain.user.UserCredentials
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.login.utilis.performLogin
import com.pdm.pokerdice.service.UserAuthService
import com.pdm.pokerdice.service.errors.AuthTokenError

/**
 * Type alias representing the signUp use case function signature.
 * @param credentials The new user's credentials.
 * @param service The service to be used for signUp.
 * @param authInfoRepo The repository to store the authentication information.
 */
typealias SignUpUseCase = suspend (
    credentials : NewUserCredentials,
    service : UserAuthService,
    authInfoRepo : AuthInfoRepo
) -> Either<AuthTokenError, AuthInfo>

/**
 * Performs the implementation of the login use case.
 * @param credentials The user's credentials.
 * @param service The service to be used for signUp.
 * @param authInfoRepo The repository to store the authentication information.
 * @return The authentication information upon successful login.
 */
suspend fun performSignUp(
    credentials: NewUserCredentials,
    service : UserAuthService,
    authInfoRepo: AuthInfoRepo
): Either<AuthTokenError, AuthInfo> {
    return try {
        val user = service.createUser(
            credentials.userName,
            credentials.email,
            credentials.password,
            credentials.inviteCode)
        when(user) {
            is Either.Failure -> {
                failure(user.value)
            }
            is Either.Success -> {
                val loginCreeds = UserCredentials(credentials.email, credentials.password)
                val loginResult = performLogin(
                    loginCreeds,
                    service,
                    authInfoRepo
                )
                loginResult
            }
        }
    } catch (e: Exception) {
        failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
    }
}