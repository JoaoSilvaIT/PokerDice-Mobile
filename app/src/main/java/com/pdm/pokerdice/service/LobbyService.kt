package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.repo.RepositoryLobby
import com.pdm.pokerdice.repo.tm.Manager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface LobbyService {
    val lobbies : Flow<List<Lobby>>

    fun joinLobby(usr: User, lobbyId: Int) : Lobby

    fun leaveLobby(usr: User, lobbyId: Int) : Boolean

    fun getUserByToken(token: String) : User?
}

class FakeLobbyService(val manager : Manager) : LobbyService {

    override val lobbies: Flow<List<Lobby>>
        get() = flow {
                while (true) {
                    emit(manager.repoLobby.findAll())
                    delay(10000)
                }
            }



    override fun getUserByToken(token: String): User? = manager.repoUser.findUserByToken(token)

    override fun joinLobby(usr: User, lobbyId: Int) : Lobby {
        val lobby = manager.repoLobby.findById(lobbyId) ?: throw Exception("Lobby not found")
        val newLobby = manager.repoLobby.joinLobby(usr, lobby)
        return newLobby
    }


    override fun leaveLobby(usr: User, lobbyId: Int) : Boolean {
        val lobby = manager.repoLobby.findById(lobbyId) ?: throw Exception("Lobby not found")
        val value = manager.repoLobby.leaveLobby(usr, lobby)
        print(value)
        return manager.repoLobby.leaveLobby(usr, lobby)
    }
}