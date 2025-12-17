package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User


interface RepositoryLobby : Repository<Lobby> {
    fun createLobby(
        name: String,
        description: String,
        minPlayers : Int,
        maxPlayers : Int,
        host: User,
    ): Lobby

    fun findByName(name: String): Lobby?

    fun joinLobby(user: User, lobby: Lobby) : Lobby

    fun leaveLobby(user: User, lobby: Lobby) : Boolean

    fun deleteLobbyByHost(host: User)

    fun deleteLobbyById(id: Int)
}