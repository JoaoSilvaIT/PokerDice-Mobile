package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.user.Token
import com.pdm.pokerdice.domain.user.TokenValidationInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.user.UserStatistics
import java.time.Instant

interface RepositoryUser : Repository<User> {
    fun createUser(
        name: String,
        email: String,
    ): User

    fun findByEmail(email: String): User?

    fun getUserById(id: Int): UserExternalInfo?

    fun getUserStats(userId: Int): UserStatistics

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun createToken(
        token: Token,
        maxTokens: Int,
    )

    fun updateTokenLastUsed(
        token: Token,
        now: Instant,
    )

    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int

    fun addBalance(
        userId: Int,
        amount: Int,
    )

}