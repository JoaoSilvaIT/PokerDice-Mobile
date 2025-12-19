package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbySettings
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.repo.RepositoryLobby

class RepoLobbyInMem : RepositoryLobby {
    val lobbies = mutableListOf(
        Lobby(
            id = 1,
            name = "LobbyTest",
            description = "This is a test lobby",
            settings = LobbySettings(numberOfRounds = 5, minPlayers = 2, maxPlayers = 4),
            host = UserExternalInfo(1, "admin", 100),
            players = setOf(
                UserExternalInfo(1, "admin", 100),
            ),
            timeout = 10L
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
            id = lid,
            name = name,
            description = description,
            settings = LobbySettings(numberOfRounds = 5, minPlayers = minPlayers, maxPlayers = maxPlayers),
            host = host,
            players = setOf(host),
            timeout = 10L
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