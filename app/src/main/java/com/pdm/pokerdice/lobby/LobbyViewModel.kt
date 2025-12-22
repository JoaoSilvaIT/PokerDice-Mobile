package com.pdm.pokerdice.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbyCountdown
import com.pdm.pokerdice.domain.lobby.LobbyEvent
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.GameService
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.service.errors.GameError
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LeaveLobbyState {
    data object Idle : LeaveLobbyState

    data object Success : LeaveLobbyState

    data class Error(
        val exception: Throwable,
    ) : LeaveLobbyState
}

interface CreateGameState {
    data object Idle : CreateGameState

    data class Success(
        val game: Game,
    ) : CreateGameState

    data class Error(
        val error: GameError,
    ) : CreateGameState
}

class LobbyViewModel(
    private val lobbyService: LobbyService,
    private val gameService: GameService,
) : ViewModel() {
    companion object {
        fun getFactory(
            service: LobbyService,
            gameService: GameService,
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
                    LobbyViewModel(lobbyService = service, gameService = gameService) as T
                } else {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
        }
    }

    private val _leaveLobbyState = MutableStateFlow<LeaveLobbyState>(LeaveLobbyState.Idle)
    val leaveLobbyState = _leaveLobbyState.asStateFlow()

    private val _createGameState = MutableStateFlow<CreateGameState>(CreateGameState.Idle)
    val createGameState = _createGameState.asStateFlow()

    private val _countdown = MutableStateFlow<LobbyCountdown?>(null)
    val countdown = _countdown.asStateFlow()

    private val _gameStarted = MutableStateFlow<Pair<Int, Int>?>(null) // (lobbyId, gameId)
    val gameStarted = _gameStarted.asStateFlow()

    private val _lobbyUpdated = MutableStateFlow<Lobby?>(null)
    val lobbyUpdated = _lobbyUpdated.asStateFlow()

    private var countdownJob: Job? = null
    private var monitoringJob: Job? = null
    private var pollingJob: Job? = null

    /**
     * Set the initial lobby state
     */
    fun setInitialLobby(lobby: Lobby) {

        _lobbyUpdated.value = lobby
    }

    /**
     * Start monitoring lobby events (countdown, game started, etc.) via SSE
     * Also starts polling to refresh lobby data every 2 seconds
     */
    fun startMonitoringLobby(lobbyId: Int) {
        monitoringJob?.cancel()
        monitoringJob =
            viewModelScope.launch {
                try {
                    lobbyService.monitorLobbyEvents(lobbyId).collect { event ->
                        when (event) {
                            is LobbyEvent.CountdownStarted -> {
                                handleCountdownStarted(event.countdown)
                            }
                            is LobbyEvent.GameStarted -> {
                                _gameStarted.value = event.lobbyId to event.gameId
                                stopCountdown()
                            }
                            is LobbyEvent.LobbyUpdated -> {
                                _lobbyUpdated.value = event.lobby
                            }
                            is LobbyEvent.ConnectionLost -> {
                                stopCountdown()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    stopCountdown()
                }
            }

        // Start polling as fallback
        startPollingLobby(lobbyId)
    }

    /**
     * Poll lobby data every 2 seconds to ensure UI stays synchronized
     */
    private fun startPollingLobby(lobbyId: Int) {
        pollingJob?.cancel()
        pollingJob =
            viewModelScope.launch {
                while (true) {
                    try {
                        delay(2000) // Poll every 2 seconds

                        when (val result = lobbyService.getLobby(lobbyId)) {
                            is Either.Success -> {
                                val lobby = result.value
                                _lobbyUpdated.value = lobby
                            }
                            is Either.Failure -> {
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }

    private fun handleCountdownStarted(countdown: LobbyCountdown) {
        // Recalculate immediately to ensure we start with accurate time
        val initialUpdate = countdown.updateRemaining()
        _countdown.value = initialUpdate

        // Start local countdown ticker for smooth UI updates
        countdownJob?.cancel()
        countdownJob =
            viewModelScope.launch {
                while (true) {
                    delay(100) // Check every 100ms for better precision

                    val current = _countdown.value ?: break

                    // Recalculate based on actual current time
                    val updated = current.updateRemaining()
                    _countdown.value = updated

                    // Check if expired right after updating
                    if (updated.isExpired) {
                        _countdown.value = null
                        break
                    }
                }
            }
    }

    private fun stopCountdown() {
        countdownJob?.cancel()
        _countdown.value = null
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        pollingJob?.cancel()
        stopCountdown()
    }

    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }

    fun leaveLobby(
        user: UserExternalInfo,
        lobbyId: Int,
    ) {
        viewModelScope.launch {
            when (lobbyService.leaveLobby(user, lobbyId)) {
                is Either.Success -> {
                    _leaveLobbyState.value = LeaveLobbyState.Success
                }
                is Either.Failure -> {
                    _leaveLobbyState.value =
                        LeaveLobbyState.Error(
                            Exception("Failed to leave lobby: ${(lobbyService.leaveLobby(user,lobbyId) as Either.Failure).value}"),
                        )
                }
            }
        }
    }

    fun createGame(
        lobby: Lobby,
        numberOfRounds: Int,
    ) {
        viewModelScope.launch {
            val result =
                gameService.createGame(
                    startedAt = System.currentTimeMillis(),
                    lobbyId = lobby.id,
                    numberOfRounds = numberOfRounds,
                    creatorId = lobby.host.id,
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
