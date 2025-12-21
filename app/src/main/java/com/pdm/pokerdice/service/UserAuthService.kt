package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.user.TokenExternalInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.errors.AuthTokenError

interface UserAuthService : Service {
    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        invite: String,
    ): Either<AuthTokenError, User>

    suspend fun createToken(
        email: String,
        password: String,
    ): Either<AuthTokenError, TokenExternalInfo>

    suspend fun getUserByToken(token: String): Either<AuthTokenError, User>

    suspend fun getUserInfo(): Either<AuthTokenError, User>

    suspend fun revokeToken(token: String): Boolean
}
