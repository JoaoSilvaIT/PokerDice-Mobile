package com.pdm.pokerdice.login

import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.login_signup.UserCredentials
import com.pdm.pokerdice.repo.tm.Manager
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Interface representing the login service.
 */
interface LoginService {

    /**
     * Logs in a user with the provided credentials.
     * @param credentials The user's credentials.
     * @return A string representing the authentication token to be used upon successful login.
     * @throws InvalidCredentialsException if the login fails.
     */
    suspend fun login(credentials: UserCredentials): Pair<User, String>
}

/**
 * Exception thrown when user credentials are invalid.
 */
class InvalidCredentialsException : Exception("Invalid user credentials provided")

/**
 * A fake implementation of the LoginService for testing purposes.
 */
class FakeLoginService(
    private val manager: Manager
) : LoginService {
    override suspend fun login(credentials: UserCredentials): Pair<User, String> {
        val user = manager.repoUser.findByEmail(credentials.email) ?: throw Exception("Invalid Credentials")
        val token = manager.repoUser.findTokenByUser(user) ?: throw Exception("Not valid user")
        return Pair(user, token)
    }
}
