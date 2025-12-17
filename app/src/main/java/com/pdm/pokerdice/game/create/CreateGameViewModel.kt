package com.pdm.pokerdice.game.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdm.pokerdice.game.GameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface CreateGameScreenState {
    data object Loading : CreateGameScreenState
    data class Error(val message: String, val lastState: CreateGameScreenState? = null) : CreateGameScreenState
    data class CreateGame(val gameId: Int) : CreateGameScreenState
}
class CreateGameViewModel (private val service: GameService): ViewModel() {
    companion object {
        fun getFactory(service: GameService) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(CreateGameViewModel::class.java)) {
                    CreateGameViewModel(service) as T
                } else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _state = MutableStateFlow<CreateGameScreenState>(CreateGameScreenState.Loading)
    val state : StateFlow<CreateGameScreenState>
        get() = _state as StateFlow<CreateGameScreenState>


}
