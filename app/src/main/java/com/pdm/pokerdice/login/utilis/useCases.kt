package com.pdm.pokerdice.login.utilis

import com.pdm.pokerdice.domain.user.AuthInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserCredentials
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.service.UserAuthService
import com.pdm.pokerdice.service.errors.AuthTokenError

/**
 * Type alias representing the login use case function signature.
 * @param credentials The user's credentials.
 * @param service The service to be used for login.
 * @param authInfoRepo The repository to store the authentication information.
 */
typealias LoginUseCase = suspend (
    credentials: UserCredentials,
    service: UserAuthService,
    authInfoRepo: AuthInfoRepo
) -> Either<AuthTokenError, AuthInfo>


/**
 * Performs the implementation of the login use case.
 * @param credentials The user's credentials.
 * @param service The service to be used for login.
 * @param authInfoRepo The repository to store the authentication information.
 * @return The authentication information upon successful login.
 */
suspend fun performLogin(
    credentials: UserCredentials,
    service : UserAuthService,
    authInfoRepo: AuthInfoRepo
): Either<AuthTokenError, AuthInfo> {
    when (val result = service.createToken(credentials.email, credentials.password)) {
        is Either.Failure -> return failure(result.value)
        is Either.Success -> {
            when (val user = service.getUserByToken(result.value.tokenValue)) {
                is Either.Failure -> return failure(user.value)
                is Either.Success -> {
                    val authInfo = AuthInfo(user.value.id, result.value.tokenValue)
                    authInfoRepo.saveAuthInfo(authInfo)
                    return success(authInfo)
                }
            }
        }
    }
}
