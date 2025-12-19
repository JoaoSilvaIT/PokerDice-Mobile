package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.repo.tm.TransactionManager
import com.pdm.pokerdice.service.errors.LobbyError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface LobbyService : Service {
    val lobbies : Flow<List<Lobby>>

    fun refreshLobbies()

    suspend fun createLobby(name: String, description: String, minPlayers: Int, maxPlayers: Int, host: UserExternalInfo) : Either<LobbyError, Lobby>

    suspend fun joinLobby(usr: UserExternalInfo, lobbyId: Int) : Either<LobbyError, Lobby>

    suspend fun leaveLobby(usr: UserExternalInfo, lobbyId: Int) : Either<LobbyError, Boolean>
}

class FakeLobbyService(val trxManager : TransactionManager , val repo : AuthInfoRepo) : LobbyService {
    private val _lobbies = MutableStateFlow(trxManager.run { repoLobby.findAll() })
    override val lobbies : StateFlow<List<Lobby>> = _lobbies.asStateFlow()
    override fun refreshLobbies() {
        _lobbies.value = trxManager.run { repoLobby.findAll() }
    }

    override suspend fun createLobby(
        name: String,
        description: String,
        minPlayers: Int,
        maxPlayers: Int,
        host: UserExternalInfo
    ): Either<LobbyError, Lobby> {
        if(name.isBlank()) return failure(LobbyError.BlankName)
        if (minPlayers < 2) return failure(LobbyError.MinPlayersTooLow)
        if (maxPlayers > 10) return failure(LobbyError.MaxPlayersTooHigh)
        if (minPlayers > maxPlayers) return failure(LobbyError.MinPlayersGreaterThanMax)

        return trxManager.run {
            if (repoLobby.findByName(name) != null) {
                return@run failure(LobbyError.NameAlreadyUsed)
            }

            val lobby = repoLobby.createLobby(name, description, minPlayers, maxPlayers, host)
            refreshLobbies() // Notify UI
            success(lobby)
        }
    }

    override suspend fun joinLobby(usr: UserExternalInfo, lobbyId: Int): Either<LobbyError, Lobby> {
        return trxManager.run {
            val lobby = repoLobby.findById(lobbyId) ?: return@run failure(LobbyError.LobbyNotFound)

            if (lobby.players.size >= lobby.settings.maxPlayers) return@run failure(LobbyError.LobbyFull)
            if (lobby.players.any { it.id == usr.id }) return@run failure(LobbyError.UserAlreadyInLobby)

            val updatedLobby = repoLobby.joinLobby(usr, lobby)
            repoLobby.save(updatedLobby)
            
            refreshLobbies() // Notify UI

            success(updatedLobby)
        }
    }


    override suspend fun leaveLobby(usr: UserExternalInfo, lobbyId: Int): Either<LobbyError, Boolean> {
        return trxManager.run {
            val lobby = repoLobby.findById(lobbyId) ?: return@run failure(LobbyError.LobbyNotFound)

            if (lobby.players.none { it.id == usr.id }) {
                return@run success(false)
            }

            if (lobby.players.size == 1) {
                repoLobby.deleteById(lobby.id)
                refreshLobbies() // Notify UI
                return@run success(true)
            }

            val updated = lobby.copy(players = lobby.players.filter { it.id != usr.id }.toSet())
            repoLobby.save(updated)
            refreshLobbies() // Notify UI
            success(false)
        }
    }

    override suspend fun getLoggedUser(): UserExternalInfo? {
        val authInfo = repo.getAuthInfo() ?: return null

        return trxManager.run {
            repoUser.getUserById(authInfo.userId)
        }
    }
}