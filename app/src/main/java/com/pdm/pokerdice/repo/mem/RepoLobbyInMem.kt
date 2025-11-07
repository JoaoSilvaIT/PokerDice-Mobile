package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.repo.RepositoryLobby
import com.pdm.pokerdice.repo.RepositoryUser
import kotlin.text.set

class RepoLobbyInMem : RepositoryLobby {
    val lobbies = mutableListOf(
        Lobby(1,
            "LobbyTest",
            "This is a test lobby",
            listOf(User(2, "jj", "")),
            2,
            User(2, "jj", ""),
            2
            )
    )
    var lid = 2

    override fun createLobby(
        name: String,
        description: String,
        maxPlayers: Int,
        rounds : Int,
        host: User
    ): Lobby {
        val newLobby = Lobby(lid, name, description, listOf(host),maxPlayers, host, rounds)
        lid++
        lobbies.add(newLobby)
        return newLobby
    }

    override fun leaveLobby(user: User, lobby: Lobby) {
        val newLobby = lobby.copy(users = (lobby.users - user))
        val index = lobbies.indexOfFirst { it.lid == lobby.lid }
        if (index != -1) {
            lobbies[index] = newLobby
        }
    }

    override fun findByName(name: String): Lobby? {
        TODO("Not yet implemented")
    }

    override fun joinLobby(user: User, lobby: Lobby): Lobby {
        val newLobby = lobby.copy(users = (lobby.users + user))
        val index = lobbies.indexOfFirst { it.lid == lobby.lid }
        if (index != -1) {
            lobbies[index] = newLobby
        }
        return newLobby
    }


    override fun deleteLobbyByHost(host: User) {
        TODO("Not yet implemented")
    }

    override fun deleteLobbyById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): Lobby? {
        return lobbies.find { it.lid == id }
    }

    override fun findAll(): List<Lobby> {
        return lobbies
    }

    override fun save(entity: Lobby) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

}