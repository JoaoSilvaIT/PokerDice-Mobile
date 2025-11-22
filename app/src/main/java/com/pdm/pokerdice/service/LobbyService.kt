package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.repo.tm.Manager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

interface LobbyService {
    val lobbies : Flow<List<Lobby>>

    fun refreshLobbies()

    fun createLobby(name: String, description: String, expectedUsers: Int, host: User, rounds : Int) : Lobby

    fun joinLobby(usr: User, lobbyId: Int) : Lobby

    fun leaveLobby(usr: User, lobbyId: Int) : Boolean
    suspend fun getLoggedUser(): User?
}

class FakeLobbyService(val manager : Manager, val repo : AuthInfoRepo) : LobbyService {
    private val _lobbies = MutableStateFlow<List<Lobby>>(manager.repoLobby.findAll())
    override val lobbies : StateFlow<List<Lobby>> = _lobbies.asStateFlow()
    override fun refreshLobbies() {
        _lobbies.value = manager.repoLobby.findAll()
    }

    override fun createLobby(
        name: String,
        description: String,
        expectedUsers: Int,
        host: User,
        rounds: Int
    ): Lobby {
        if(name.isBlank()) throw Exception("Lobby name cannot be empty")
        if(description.isBlank()) throw Exception("Lobby description cannot be empty")
        if (expectedUsers < 2) throw Exception("The Game need to have at least 2 players")
        if ((rounds % expectedUsers) != 0) throw Exception("The number of rounds must be multiple of the number of players")
        return manager.repoLobby.createLobby(name, description, expectedUsers, rounds, host)
    }

    override fun joinLobby(usr: User, lobbyId: Int) : Lobby {
        val lobby = manager.repoLobby.findById(lobbyId) ?: throw Exception("Lobby not found")
        val newLobby = manager.repoLobby.joinLobby(usr, lobby)
        refreshLobbies()
        return newLobby
    }


    override fun leaveLobby(usr: User, lobbyId: Int) : Boolean {
        val lobby = manager.repoLobby.findById(lobbyId) ?: throw Exception("Lobby not found")
        if (manager.repoLobby.leaveLobby(usr, lobby)) {
            refreshLobbies()
            return true
        }
        return false
    }

    override suspend fun getLoggedUser(): User? {
        val authInfo = repo.getAuthInfo()
        return if (authInfo != null) {
            manager.repoUser.findUserByToken(authInfo.authToken)
        } else null
    }
}