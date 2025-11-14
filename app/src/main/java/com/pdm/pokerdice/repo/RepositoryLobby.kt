package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User


interface RepositoryLobby : Repository<Lobby> {
    fun createLobby(
        name: String,
        description: String,
        maxPlayers: Int,
        rounds : Int,
        host: User,
    ): Lobby

    fun findByName(name: String): Lobby?

    fun joinLobby(user: User, lobby: Lobby) : Lobby

    fun leaveLobby(user: User, lobby: Lobby) : Boolean

    fun deleteLobbyByHost(host: User)

    fun deleteLobbyById(id: Int)
}