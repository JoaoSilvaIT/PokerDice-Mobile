package com.pdm.pokerdice.ui.lobby.lobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.service.LobbyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LobbiesViewModel (private val lobbyService: LobbyService) : ViewModel() {

    companion object {
        fun getFactory(service: LobbyService
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(LobbiesViewModel::class.java)) {
                    LobbiesViewModel(lobbyService = service) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _lobbies = MutableStateFlow<List<Lobby>>(emptyList())
    val lobbies = _lobbies.asStateFlow()

    init {
        viewModelScope.launch {
            lobbyService.lobbies
                .distinctUntilChanged()
                .collectLatest { _lobbies.value = it }
        }
    }

}