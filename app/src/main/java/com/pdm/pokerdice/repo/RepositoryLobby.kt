package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo


interface RepositoryLobby : Repository<Lobby> {
    fun createLobby(
        name: String,
        description: String,
        minPlayers : Int,
        maxPlayers : Int,
        host: UserExternalInfo,
    ): Lobby

    fun findByName(name: String): Lobby?

    fun joinLobby(user: UserExternalInfo, lobby: Lobby) : Lobby

    fun leaveLobby(user: UserExternalInfo, lobby: Lobby) : Boolean

    fun deleteLobbyByHost(host: UserExternalInfo)

    fun deleteLobbyById(id: Int)
}