package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.User
import java.time.Instant

interface RepositoryUser : Repository<User> {
    fun createUser(
        name: String,
        email: String,
    ): User

    fun findByEmail(email: String): User?

    fun findUserByToken(token: String): User?

    fun addToken(token: String): String

}