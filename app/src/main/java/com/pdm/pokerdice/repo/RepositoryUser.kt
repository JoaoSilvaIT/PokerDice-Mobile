package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.User

interface RepositoryUser : Repository<User> {

    fun createUser(
        name: String,
        email: String,
    ): User

    fun findByEmail(email: String): User?
    fun findByName(name: String): User?
}