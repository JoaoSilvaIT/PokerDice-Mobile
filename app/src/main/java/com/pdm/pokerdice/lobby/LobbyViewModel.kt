package com.pdm.pokerdice.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.GameService
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.service.errors.GameError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LeaveLobbyState {
    data object Idle : LeaveLobbyState
    data object Success : LeaveLobbyState
    data class Error(val exception: Throwable) : LeaveLobbyState
}

interface CreateGameState {
    data object Idle : CreateGameState
    data class Success(val game: Game) : CreateGameState
    data class Error(val error : GameError) : CreateGameState
}

class LobbyViewModel (private val lobbyService: LobbyService, private val gameService: GameService, private val repo : AuthInfoRepo) : ViewModel() {

    companion object {
        fun getFactory(service: LobbyService, repo: AuthInfoRepo, gameService: GameService
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
                    LobbyViewModel(lobbyService = service, repo = repo, gameService = gameService) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _leaveLobbyState = MutableStateFlow<LeaveLobbyState>(LeaveLobbyState.Idle)
    val leaveLobbyState = _leaveLobbyState.asStateFlow()

    private val _createGameState = MutableStateFlow<CreateGameState>(CreateGameState.Idle)
    val createGameState = _createGameState.asStateFlow()


    fun leaveLobby(user: UserExternalInfo, lobbyId: Int) {
        viewModelScope.launch {
            when(lobbyService.leaveLobby(user,lobbyId)) {
                is Either.Success -> {
                    _leaveLobbyState.value = LeaveLobbyState.Success
                }
                is Either.Failure -> {
                    _leaveLobbyState.value =
                        LeaveLobbyState.Error(Exception("Failed to leave lobby: ${(lobbyService.leaveLobby(user,lobbyId) as Either.Failure).value}"))
                }
            }
        }
    }

    fun createGame(lobby: Lobby, numberOfRounds : Int) {
        viewModelScope.launch {
            val result = gameService.createGame(
                startedAt = System.currentTimeMillis(),
                lobbyId = lobby.id,
                numberOfRounds = numberOfRounds,
                creatorId = lobby.host.id
            )

            when (result) {
                is Either.Success -> {
                    _createGameState.value = CreateGameState.Success(result.value)
                }
                is Either.Failure -> {
                   _createGameState.value = CreateGameState.Error(result.value)
                }
            }
        }
    }
}