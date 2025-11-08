package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.repo.RepositoryUser

class RepoUserInMem : RepositoryUser {

    val users = mutableListOf(
        User(1, "admin", "admin@gmail.com", "admin123"),
        User(2, "user1", "user1@test.com", "password1"),
        User(3, "user2", "user2@test.com", "password2")
    )
    var uid = 4

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
        return users.find { it.email == email }
    }

    override fun findByName(name: String): User? {
        return users.find { it.name == name }
    }

    fun createUserWithPassword(name: String, password: String): User {
        val newUser = User(uid, name, password = password)
        uid++
        users.add(newUser)
        return newUser
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