package com.pdm.pokerdice.ui.lobby.lobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.service.LobbyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

sealed interface LobbiesScreenState {
    data object Loading : LobbiesScreenState
    data class ViewLobbies(val lobbies : List<Lobby>) : LobbiesScreenState
    data class JoinLobby(val lobby: Lobby, val user : User) : LobbiesScreenState
    data class Error(val message: String, val lastState: LobbiesScreenState? = null) : LobbiesScreenState
}
class LobbiesViewModel (private val service: LobbyService) : ViewModel() {
    companion object {
        fun getFactory(service: LobbyService) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbiesViewModel::class.java)) {
                    LobbiesViewModel(service) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    private val _state = MutableStateFlow<LobbiesScreenState>(LobbiesScreenState.Loading)
    val state : StateFlow<LobbiesScreenState>
        get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            service.lobbies
                .distinctUntilChanged()
                .collect { lobbies ->
                    _state.value = LobbiesScreenState.ViewLobbies(lobbies)
                }
        }
    }
    fun joinLobby(lobbyId: Int) {
        viewModelScope.launch {
            val user = service.getLoggedUser() ?: run {
                _state.value = LobbiesScreenState.Error("User not logged in", _state.value)
                return@launch
            }
            try {
                val lobby = service.joinLobby(user, lobbyId)
                _state.value = LobbiesScreenState.JoinLobby(lobby, user)
            } catch (e: Exception) {
                _state.value = LobbiesScreenState.Error("Failed to join lobby: ${e.message}", _state.value)
            }
        }
    }
}