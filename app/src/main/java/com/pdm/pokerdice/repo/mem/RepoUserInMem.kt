package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.user.Token
import com.pdm.pokerdice.domain.user.TokenValidationInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.user.UserStatistics
import com.pdm.pokerdice.repo.RepositoryUser
import java.time.Instant

const val DEFAULT_BALANCE = 100

class RepoUserInMem : RepositoryUser {
    val users =
        mutableListOf(
            User(
                id = 1,
                name = "admin",
                email = "admin@gmail.com",
                password = "1234",
                DEFAULT_BALANCE,
                UserStatistics(
                    gamesPlayed = 0,
                    wins = 0,
                    losses = 0,
                    winRate = 0.0,
                ),
            ),
            User(
                id = 2,
                name = "jaco",
                email = "jaco@gmail.com",
                password = "1234",
                DEFAULT_BALANCE,
                UserStatistics(
                    gamesPlayed = 0,
                    wins = 0,
                    losses = 0,
                    winRate = 0.0,
                ),
            ),
        )

    val tokens = mutableListOf<Token>()
    var uid = 2

    override fun createUser(
        name: String,
        email: String,
        password: String,
    ): User {
        val newUser =
            User(
                uid,
                name,
                email,
                password,
                DEFAULT_BALANCE,
                UserStatistics(
                    gamesPlayed = 0,
                    wins = 0,
                    losses = 0,
                    winRate = 0.0,
                ),
            )
        uid++
        users.add(newUser)
        return newUser
    }

    override fun addAll(entities: List<User>) {
        users.addAll(entities)
    }

    override fun findByEmail(email: String): User? = users.find { it.email == email }

    override fun getUserById(id: Int): UserExternalInfo? =
        users.find { it.id == id }?.let {
            UserExternalInfo(
                id = it.id,
                name = it.name,
                balance = it.balance,
            )
        }

    override fun getUserStats(userId: Int): UserStatistics? = users.find { it.id == userId }?.statistics

    override fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>? =
        tokens.firstOrNull { it.tokenValidationInfo == tokenValidationInfo }?.let {
            val user = findById(it.userId)
            requireNotNull(user)
            user to it
        }

    override fun createToken(
        token: Token,
        maxTokens: Int,
    ) {
        val nrOfTokens = tokens.count { it.userId == token.userId }

        // Remove the oldest token if we have achieved the maximum number of tokens
        if (nrOfTokens >= maxTokens) {
            tokens
                .filter { it.userId == token.userId }
                .minByOrNull { it.lastUsedAt }!!
                .also { tk -> tokens.removeIf { it.tokenValidationInfo == tk.tokenValidationInfo } }
        }
        tokens.add(token)
    }

    override fun updateTokenLastUsed(
        token: Token,
        now: Instant,
    ) {
        tokens.removeIf { it.tokenValidationInfo == token.tokenValidationInfo }
        tokens.add(token.copy(lastUsedAt = now))
    }

    override fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int {
        val initialSize = tokens.size
        tokens.removeIf { it.tokenValidationInfo == tokenValidationInfo }
        return initialSize - tokens.size
    }

    override fun addBalance(
        userId: Int,
        amount: Int,
    ) {
        TODO()
    }

    override fun findById(id: Int): User? = users.find { it.id == id }

    override fun findAll(): List<User> = users.toList()

    override fun save(entity: User) {
        users.removeIf { it.id == entity.id }
        users.add(entity)
    }

    override fun deleteById(id: Int) {
        users.removeIf { it.id == id }
    }

    override fun clear() {
        users.clear()
        tokens.clear()
    }
}
