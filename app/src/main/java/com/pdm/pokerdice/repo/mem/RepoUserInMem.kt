package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.user.Token
import com.pdm.pokerdice.domain.user.TokenValidationInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.user.UserStatistics
import com.pdm.pokerdice.repo.RepositoryUser
import java.time.Instant

class RepoUserInMem : RepositoryUser {

    val users = mutableListOf(
        User(1, "admin", "admin@gmail.com")
    )

    val tokens = mutableListOf(
        "fake_auth_token_1234567"
    )
    var uid = 2

    override fun createUser(
        name: String,
        email: String,
    ): User {
        val newUser = User(uid, name, email)
        uid++
        users.add(newUser)
        return newUser
    }


    override fun addAll(entities: List<User>) {
        users.addAll(entities)
    }

    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Int): UserExternalInfo? {
        TODO("Not yet implemented")
    }

    override fun getUserStats(userId: Int): UserStatistics {
        TODO("Not yet implemented")
    }

    override fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>? {
        TODO("Not yet implemented")
    }

    override fun createToken(token: Token, maxTokens: Int) {
        TODO("Not yet implemented")
    }

    override fun updateTokenLastUsed(token: Token, now: Instant) {
        TODO("Not yet implemented")
    }

    override fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int {
        TODO("Not yet implemented")
    }

    override fun addBalance(userId: Int, amount: Int) {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): User? {
        return users.find { it.id == id }
    }

    override fun findAll(): List<User> {
        TODO("Not yet implemented")
    }

    override fun save(entity: User) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }


}