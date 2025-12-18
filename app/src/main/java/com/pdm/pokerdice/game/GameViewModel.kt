package com.pdm.pokerdice.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdm.pokerdice.lobby.lobbies.LobbiesScreenState
import com.pdm.pokerdice.service.GameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface GameState {
    data object Loading : GameState
}
class GameViewModel (private val service: GameService) : ViewModel() {
    companion object {
        fun getFactory(service: GameService) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                    GameViewModel(service) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _state = MutableStateFlow<GameState>(GameState.Loading)
    val state : StateFlow<GameState>
        get() = _state.asStateFlow()
}