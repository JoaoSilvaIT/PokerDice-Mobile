package com.pdm.pokerdice.ui.lobby.lobbyIndividual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LeaveLobbyState {
    data object Idle : LeaveLobbyState
    data class Success(val message: String) : LeaveLobbyState
    data class Error(val exception: Throwable) : LeaveLobbyState
}

class LobbyViewModel (private val lobbyService: LobbyService) : ViewModel() {

    companion object {
        fun getFactory(service: LobbyService
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
                    LobbyViewModel(lobbyService = service) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _leaveLobbyState = MutableStateFlow<LeaveLobbyState>(LeaveLobbyState.Idle)
    val leaveLobbyState = _leaveLobbyState.asStateFlow()

    fun leaveLobby(user: User, lobbyId: Int) {
        viewModelScope.launch {
            try {
                lobbyService.leaveLobby(user,lobbyId)
                _leaveLobbyState.value = LeaveLobbyState.Success("Left lobby successfully")
            } catch (e: Exception) {
                _leaveLobbyState.value = LeaveLobbyState.Error(e)
            }
        }
    }

}