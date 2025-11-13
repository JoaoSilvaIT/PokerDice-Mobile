package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.User
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

    override fun findUserByToken(token: String): User? {
        val index = tokens.indexOf(token)
        return if (index != -1) {
            users[index]
        } else {
            null
        }
    }

    override fun addToken(token: String): String {
        tokens.add(token)
        return token
    }

    override fun addAll(entities: List<User>) {
        users.addAll(entities)
    }

    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): User? {
        return users.find { it.uid == id }
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