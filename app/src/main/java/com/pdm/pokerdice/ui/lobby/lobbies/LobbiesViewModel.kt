package com.pdm.pokerdice.ui.lobby.lobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.service.LobbyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

sealed interface JoinLobbyState {
    data object Idle : JoinLobbyState
    data class Success(val lobby : Lobby) : JoinLobbyState
    data class Error(val exception: Throwable) : JoinLobbyState
}
class LobbiesViewModel (private val service: LobbyService, private val repo : AuthInfoRepo) : ViewModel() {
    companion object {
        fun getFactory(service: LobbyService, repo: AuthInfoRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbiesViewModel::class.java)) {
                    LobbiesViewModel(service, repo) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _lobbies = MutableStateFlow<List<Lobby>>(emptyList())
    val lobbies = _lobbies.asStateFlow()

    private val _joinLobbyState = MutableStateFlow<JoinLobbyState>(JoinLobbyState.Idle)
    val joinLobbyState = _joinLobbyState.asStateFlow()

    init {
        viewModelScope.launch {
            service.lobbies
                .distinctUntilChanged()
                .collectLatest { _lobbies.value = it }
        }
    }

    suspend fun getLoggedUser(): User? {
        val authInfo = repo.getAuthInfo()
        return if (authInfo != null) {
            service.getUserByToken(authInfo.authToken)
        } else null
    }

    fun joinLobby(lobbyId: Int) {
        viewModelScope.launch {
            val user = getLoggedUser() ?: run {
                _joinLobbyState.value = JoinLobbyState.Error(Exception("User not logged in"))
                return@launch
            }
            try {
                val lobby = service.joinLobby(user, lobbyId)
                _joinLobbyState.value = JoinLobbyState.Success(lobby)
            } catch (e: Exception) {
                _joinLobbyState.value = JoinLobbyState.Error(e)
            }
        }
    }
}