package com.pdm.pokerdice.signUp

import com.pdm.pokerdice.domain.user.NewUserCredentials
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.repo.tm.Transaction

/**
 * Interface representing the login service.
 */
interface SignUpService {

    /**
     * Sign's up in a user with the provided credentials.
     * @param credentials The user's credentials.
     * @return A string representing the authentication token to be used upon successful sign up.
     * @throws com.pdm.pokerdice.login.InvalidCredentialsException if the login fails.
     */
    suspend fun signUp(credentials: NewUserCredentials): Pair<User,String>
}

/**
 * A fake implementation of the SignUpService for testing purposes.
 */
class FakeSignUpService(
    private val manager: Transaction
) : SignUpService {
    override suspend fun signUp(credentials: NewUserCredentials): Pair<User,String> {
        val user = manager.repoUser.createUser(credentials.userName, credentials.email)
        val token = manager.repoUser.addToken("fake_auth_token_123456")
        return Pair(user, token)
    }
}