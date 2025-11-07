package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.repo.RepositoryLobby
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface LobbyService {
    val lobbies : Flow<List<Lobby>>
}

class FakeLobbyService(val repositoryLobby: RepositoryLobby) : LobbyService {
    override val lobbies: Flow<List<Lobby>>
        get() = flow {
            while (true) {
                emit(repositoryLobby.findAll())
                delay(10000)
            }
        }
}