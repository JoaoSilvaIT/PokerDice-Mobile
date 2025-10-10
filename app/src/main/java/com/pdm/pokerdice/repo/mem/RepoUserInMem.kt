package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.repo.RepositoryUser

class RepoUserInMem : RepositoryUser {

    val users = mutableListOf(
        User(1, "admin", "admin@gmail.com")
    )
    var uid = 2

    override fun createUser(
        name: String,
        email: String
    ): User {
        val newUser = User(uid, name, email)
        uid++
        users.add(newUser)
        return newUser
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