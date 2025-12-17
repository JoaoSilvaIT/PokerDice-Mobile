package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.repo.RepositoryLobby

class RepoLobbyInMem : RepositoryLobby {
    val lobbies = mutableListOf(
        Lobby(
            1,
            "LobbyTest",
            "This is a test lobby",
            2,
            4,
            setOf(
                UserExternalInfo(1, "admin", 100),
            ),
            UserExternalInfo(1, "admin", 100)
        )
    )
    var lid = 2

    override fun createLobby(
        name: String,
        description: String,


        minPlayers: Int,
        maxPlayers: Int,
        host: UserExternalInfo
    ): Lobby {
        val newLobby = Lobby(
            lid,
            name,
            description,
            minPlayers,
            maxPlayers,
            setOf(
                host
            ),
            host
        )
        lid++
        lobbies.add(newLobby)
        return newLobby
    }

    override fun leaveLobby(user: UserExternalInfo, lobby: Lobby) : Boolean {
        val newLobby = lobby.copy(players =  (lobby.players - user))
        val index = lobbies.indexOfFirst { it.id == lobby.id }
        if (index != -1) {
            lobbies[index] = newLobby
        }
        return lobbies[index] == newLobby
    }

    override fun addAll(entities: List<Lobby>) {
        lobbies.addAll(entities)
    }

    override fun findByName(name: String): Lobby? {
        return lobbies.find { it.name == name }
    }

    override fun joinLobby(user: UserExternalInfo, lobby: Lobby): Lobby {
        val newLobby = lobby.copy(players = (lobby.players + user))
        val index = lobbies.indexOfFirst { it.id == lobby.id }
        if (index != -1) {
            lobbies[index] = newLobby
        }
        return newLobby
    }


    override fun deleteLobbyByHost(host: UserExternalInfo) {
        lobbies.removeIf { it.host == host }
    }

    override fun deleteLobbyById(id: Int) {
        lobbies.removeIf { it.id == id }
    }

    override fun findById(id: Int): Lobby? {
        return lobbies.find { it.id == id }
    }

    override fun findAll(): List<Lobby> {
        return lobbies
    }

    override fun save(entity: Lobby) {
        lobbies.removeIf { it.id == entity.id }
        lobbies.add(entity)
    }

    override fun deleteById(id: Int) {
        lobbies.removeIf { it.id == id }
    }

    override fun clear() {
        lobbies.clear()
    }

}