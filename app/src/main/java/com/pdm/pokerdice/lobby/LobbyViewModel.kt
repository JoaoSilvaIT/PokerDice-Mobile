package com.pdm.pokerdice.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbyInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.LobbyService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Represents the state of the Lobby screen.
 */
sealed class LobbyScreenState {
    object Loading : LobbyScreenState()
    object Configuring : LobbyScreenState() // User is creating a new lobby
    data class Waiting(val lobby: Lobby, val user: UserExternalInfo) : LobbyScreenState() // User is in a lobby
    data class Error(val message: String) : LobbyScreenState()
}

class LobbyViewModel(
    private val service: LobbyService,
    private val repo: AuthInfoRepo
) : ViewModel() {

    var screenState by mutableStateOf<LobbyScreenState>(LobbyScreenState.Loading)
        private set

    private var monitoringJob: Job? = null

    /**
     * Initializes the ViewModel.
     * @param initialLobby If provided, we start in Waiting state. If null, we start in Configuring state.
     * @param initialUser The logged user info (optional, usually fetched from repo).
     */
    fun initialize(initialLobby: Lobby?, initialUser: UserExternalInfo?) {
        viewModelScope.launch {
            val user = initialUser ?: service.getLoggedUser()
            
            if (user == null) {
                screenState = LobbyScreenState.Error("User not logged in")
                return@launch
            }

            if (initialLobby != null) {
                enterWaitingState(initialLobby, user)
            } else {
                screenState = LobbyScreenState.Configuring
            }
        }
    }

    private fun enterWaitingState(lobby: Lobby, user: UserExternalInfo) {
        screenState = LobbyScreenState.Waiting(lobby, user)
        startMonitoring(lobby.id, user)
    }

    private fun startMonitoring(lobbyId: Int, user: UserExternalInfo) {
        monitoringJob?.cancel()
        monitoringJob = viewModelScope.launch {
            service.lobbies.collect { lobbiesList ->
                val updatedLobby = lobbiesList.find { it.id == lobbyId }
                if (updatedLobby != null) {
                    // Update state if we are still in Waiting
                    if (screenState is LobbyScreenState.Waiting) {
                        screenState = LobbyScreenState.Waiting(updatedLobby, user)
                    }
                } else {
                    // Lobby might have been deleted? Keep current state or handle error?
                    // For now, keep current state to avoid jarring UI changes if poll fails once
                }
            }
        }
    }

    fun createLobby(info: LobbyInfo) {
        viewModelScope.launch {
            // Fetch user again to be safe
            val user = service.getLoggedUser() ?: return@launch

            screenState = LobbyScreenState.Loading
            
            val result = service.createLobby(
                info.name,
                info.description,
                info.minPlayers,
                info.maxPlayers,
                user
            )

            when (result) {
                is Either.Success -> {
                    enterWaitingState(result.value, user)
                }
                is Either.Failure -> {
                    screenState = LobbyScreenState.Error("Failed to create: ${result.value}")
                }
            }
        }
    }

    fun leaveLobby() {
        monitoringJob?.cancel() // Stop monitoring
        viewModelScope.launch {
            val currentState = screenState
            if (currentState is LobbyScreenState.Waiting) {
                val result = service.leaveLobby(currentState.user, currentState.lobby.id)
                when(result) {
                    is Either.Success -> {
                        // Navigation handled by UI
                    }
                    is Either.Failure -> {
                        screenState = LobbyScreenState.Error("Failed to leave: ${result.value}")
                    }
                }
            }
        }
    }
    
    fun backToConfig() {
        monitoringJob?.cancel()
        screenState = LobbyScreenState.Configuring
    }

    companion object {
        fun getFactory(service: LobbyService, repo: AuthInfoRepo): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LobbyViewModel(service, repo)
            }
        }
    }
}
