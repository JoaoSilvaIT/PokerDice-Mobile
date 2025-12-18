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
                screenState = LobbyScreenState.Waiting(initialLobby, user)
            } else {
                screenState = LobbyScreenState.Configuring
            }
        }
    }

    fun createLobby(info: LobbyInfo) {
        viewModelScope.launch {
            val currentState = screenState
            // Ensure we only create if we are configuring or loading
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
                    screenState = LobbyScreenState.Waiting(result.value, user)
                }
                is Either.Failure -> {
                    screenState = LobbyScreenState.Error("Failed to create: ${result.value}")
                    // Optionally revert to Configuring after a delay or user action
                }
            }
        }
    }

    fun leaveLobby() {
        viewModelScope.launch {
            val currentState = screenState
            if (currentState is LobbyScreenState.Waiting) {
                // Optimistic update or waiting?
                // For now, call service
                val result = service.leaveLobby(currentState.user, currentState.lobby.id)
                when(result) {
                    is Either.Success -> {
                        // Navigation should handle exit, but state can go to idle/finish
                        // Actually, Activity should observe this or we have a specialized event?
                        // For simplicity, we can set state to Configuring or send a navigation event via a separate flow/channel if needed.
                        // Or just Activity finishes.
                    }
                    is Either.Failure -> {
                        screenState = LobbyScreenState.Error("Failed to leave: ${result.value}")
                    }
                }
            }
        }
    }
    
    fun backToConfig() {
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
