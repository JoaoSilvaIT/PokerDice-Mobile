package com.pdm.pokerdice.ui.lobby.lobbyCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbyInfo
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LobbyCreationState {
    data object Idle : LobbyCreationState
    data class Success(val lobby : Lobby) : LobbyCreationState
    data class Error(val exception: Throwable) : LobbyCreationState
}

class LobbyCreationViewModel (private val service : LobbyService, private val repo: AuthInfoRepo) : ViewModel() {
    companion object {
        fun getFactory(service: LobbyService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbyCreationViewModel::class.java)) {
                    LobbyCreationViewModel(service, repo) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _createLobbyState = MutableStateFlow<LobbyCreationState>(LobbyCreationState.Idle)
    val createLobbyState = _createLobbyState.asStateFlow()

    fun createLobby(info: LobbyInfo) {
        viewModelScope.launch {
            val user = service.getLoggedUser() ?: run {
                _createLobbyState.value = LobbyCreationState.Error(Exception("User not logged in"))
                return@launch
            }
            try {
                val lobby = service.createLobby(
                    info.name,
                    info.description,
                    info.expectedPlayers,
                    user,
                    info.rounds
                )
                _createLobbyState.value = LobbyCreationState.Success(lobby)
            } catch (e: Exception) {
                _createLobbyState.value = LobbyCreationState.Error(e)
            }
        }
    }
}