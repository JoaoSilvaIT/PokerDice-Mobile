package com.pdm.pokerdice.lobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.LobbyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Represents the various visual states of the Lobbies Screen.
 */
sealed interface LobbiesScreenState {
    data object Loading : LobbiesScreenState
    data class ViewLobbies(val lobbies : List<Lobby>) : LobbiesScreenState
    data class JoinLobby(val lobby: Lobby, val user: UserExternalInfo) : LobbiesScreenState
    data class Error(val message: String, val lastState: LobbiesScreenState? = null) : LobbiesScreenState
}

class LobbiesViewModel(private val service: LobbyService) : ViewModel() {

    private val _state = MutableStateFlow<LobbiesScreenState>(LobbiesScreenState.Loading)
    val state: StateFlow<LobbiesScreenState> = _state.asStateFlow()

    init {
        fetchLobbies()
    }

    private fun fetchLobbies() {
        viewModelScope.launch {
            try {
                service.lobbies
                    .distinctUntilChanged()
                    .collect { lobbies ->
                        _state.value = LobbiesScreenState.ViewLobbies(lobbies)
                    }
            } catch (e: Exception) {
                _state.value = LobbiesScreenState.Error("Failed to load: ${e.message}")
            }
        }
    }

    fun joinLobby(lobbyId: Int) {
        viewModelScope.launch {
            val user = service.getLoggedUser() ?: run {
                _state.value = LobbiesScreenState.Error("User not logged in", _state.value)
                return@launch
            }
            
            when (val result = service.joinLobby(user, lobbyId)) {
                is Either.Success -> {
                    _state.value = LobbiesScreenState.JoinLobby(result.value, user)
                }
                is Either.Failure -> {
                    _state.value = LobbiesScreenState.Error(
                        "Failed to join lobby: ${result.value}",
                        _state.value
                    )
                }
            }
        }
    }

    fun resetToIdle() {
        val currentState = _state.value
        if (currentState is LobbiesScreenState.JoinLobby) {
            // Re-fetch or go back to previous state
            fetchLobbies()
        }
    }

    companion object {
        fun getFactory(service: LobbyService): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LobbiesViewModel(service)
            }
        }
    }
}
