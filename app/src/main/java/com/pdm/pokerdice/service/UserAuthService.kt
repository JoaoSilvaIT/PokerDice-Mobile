package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.user.Token
import com.pdm.pokerdice.domain.user.TokenEncoder
import com.pdm.pokerdice.domain.user.TokenExternalInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UsersDomainConfig
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.repo.tm.TransactionManager
import com.pdm.pokerdice.service.errors.AuthTokenError
import java.security.SecureRandom
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.Base64.getUrlDecoder
import java.util.Base64.getUrlEncoder

class UserAuthService(
    private val tokenEncoder: TokenEncoder,
    private val config: UsersDomainConfig,
    private val trxManager: TransactionManager,
    private val clock: Clock
){
    fun createUser(
        name: String,
        email: String,
        password: String,
    ) : Either<AuthTokenError, User> {
        if (name.isBlank()) return failure(AuthTokenError.BlankName)
        if (email.isBlank()) return failure(AuthTokenError.BlankEmail)
        if (password.isBlank()) return failure(AuthTokenError.BlankPassword)

        val emailTrimmed = email.trim()

        return trxManager.run {
            if (repoUser.findByEmail(emailTrimmed) != null) {
                return@run failure(AuthTokenError.EmailAlreadyInUse)
            }
            val newUser = repoUser.createUser(name.trim(), emailTrimmed)
            success(newUser)
        }

    }

    fun createToken(
        email: String,
        password: String
    ): Either<AuthTokenError, TokenExternalInfo> {
        if (email.isBlank()) return failure(AuthTokenError.BlankEmail)
        if (password.isBlank()) return failure(AuthTokenError.BlankPassword)

        return trxManager.run {
            val user =
                repoUser.findByEmail(email.trim())
                    ?: return@run failure(AuthTokenError.UserNotFoundOrInvalidCredentials)

            if (user.password != password) {
                return@run failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
            }

            val tokenValue = generateTokenValue()
            val now = clock.instant()
            val newToken =
                Token(
                    tokenEncoder.createValidationInformation(tokenValue),
                    user.id,
                    createdAt = now,
                    lastUsedAt = now,
                )
            repoUser.createToken(newToken, config.maxTokensPerUser)

            success(TokenExternalInfo(tokenValue, getTokenExpiration(newToken)))
        }
    }

    fun getUserByToken(token: String): User? {
        if (!canBeToken(token)) {
            return null
        }

        return trxManager.run {
            val tokenValidationInfo = tokenEncoder.createValidationInformation(token)
            val userAndToken = repoUser.getTokenByTokenValidationInfo(tokenValidationInfo)

            if (userAndToken != null && isTokenTimeValid(clock, userAndToken.second)) {
                repoUser.updateTokenLastUsed(userAndToken.second, clock.instant())
                userAndToken.first
            } else {
                null
            }
        }
    }

    private fun isTokenTimeValid(
        clock: Clock,
        token: Token,
    ): Boolean {
        val now = clock.instant()
        return token.createdAt <= now &&
                Duration.between(token.createdAt, now) <= config.tokenTtl &&
                Duration.between(token.lastUsedAt, now) <= config.tokenRollingTtl
    }
    private fun generateTokenValue(): String =
        ByteArray(config.tokenSizeInBytes).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            getUrlEncoder().encodeToString(byteArray)
        }

    private fun getTokenExpiration(token: Token): Instant {
        val absoluteExpiration = token.createdAt + config.tokenTtl
        val rollingExpiration = token.lastUsedAt + config.tokenRollingTtl
        return if (absoluteExpiration < rollingExpiration) {
            absoluteExpiration
        } else {
            rollingExpiration
        }
    }

    fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = tokenEncoder.createValidationInformation(token)
        return trxManager.run {
            repoUser.removeTokenByValidationInfo(tokenValidationInfo)
            true
        }
    }

    private fun canBeToken(token: String): Boolean =
        try {
            getUrlDecoder().decode(token).size == config.tokenSizeInBytes
        } catch (ex: IllegalArgumentException) {
            false
        }
}