package com.pdm.pokerdice.login_signup

import android.view.SurfaceControl
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.repo.Repository
import com.pdm.pokerdice.repo.RepositoryUser
import com.pdm.pokerdice.repo.tm.Manager

/**
 * Interface representing the login service.
 */
interface SignUpService {

    /**
     * Sign's up in a user with the provided credentials.
     * @param credentials The user's credentials.
     * @return A string representing the authentication token to be used upon successful sign up.
     * @throws InvalidCredentialsException if the login fails.
     */
    suspend fun signUp(credentials: NewUserCredentials): Pair<User,String>
}

/**
 * A fake implementation of the SignUpService for testing purposes.
 */
class FakeSignUpService(
    private val manager: Manager
) : SignUpService {
    override suspend fun signUp(credentials: NewUserCredentials): Pair<User,String> {
        val user = manager.repoUser.createUser(credentials.userName, credentials.email)
        val token = manager.repoUser.addToken("fake_auth_token_123456")
        return Pair(user, token)
    }
}