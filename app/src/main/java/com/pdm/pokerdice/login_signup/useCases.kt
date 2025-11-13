package com.pdm.pokerdice.login_signup

/**
 * Type alias representing the login use case function signature.
 * @param credentials The user's credentials.
 * @param loginService The service to be used for login.
 * @param authInfoRepo The repository to store the authentication information.
 */
typealias LoginUseCase = suspend (
    credentials: UserCredentials,
    loginService: LoginService,
    authInfoRepo: AuthInfoRepo
) -> AuthInfo

/**
 * Type alias representing the login use case function signature.
 * @param credentials The new user's credentials.
 * @param signUpService The service to be used for signUp.
 * @param authInfoRepo The repository to store the authentication information.
 */
typealias SignUpUseCase = suspend (
    credentials : NewUserCredentials,
    signUpService : SignUpService,
    authInfoRepo : AuthInfoRepo
) -> AuthInfo

/**
 * Performs the implementation of the login use case.
 * @param credentials The user's credentials.
 * @param loginService The service to be used for login.
 * @param authInfoRepo The repository to store the authentication information.
 * @return The authentication information upon successful login.
 */
/*
suspend fun performLogin(
    credentials: UserCredentials,
    loginService: LoginService,
    authInfoRepo: AuthInfoRepo
): AuthInfo {
    val authToken = loginService.login(credentials)
    val authInfo = AuthInfo(userEmail = credentials.email, authToken = authToken)
    authInfoRepo.saveAuthInfo(authInfo)
    return authInfo
}

 */

/**
 * Performs the implementation of the login use case.
 * @param credentials The user's credentials.
 * @param signUpService The service to be used for signUp.
 * @param authInfoRepo The repository to store the authentication information.
 * @return The authentication information upon successful login.
 */
suspend fun performSignUp(
    credentials: NewUserCredentials,
    signUpService: SignUpService,
    authInfoRepo: AuthInfoRepo
): AuthInfo {
    val (user, token) = signUpService.signUp(credentials = credentials)
    val authInfo = AuthInfo(user.uid, token)
    authInfoRepo.saveAuthInfo(authInfo)
    return authInfo
}