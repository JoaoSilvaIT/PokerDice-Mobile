    package com.pdm.pokerdice.ui.lobby.lobbyIndividual

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import com.pdm.pokerdice.domain.User
    import com.pdm.pokerdice.login_signup.AuthInfoRepo
    import com.pdm.pokerdice.service.LobbyService
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch

    interface LeaveLobbyState {
        data object Idle : LeaveLobbyState
        data object Success : LeaveLobbyState
        data class Error(val exception: Throwable) : LeaveLobbyState
    }

    class LobbyViewModel (private val service: LobbyService, private val repo : AuthInfoRepo) : ViewModel() {

        companion object {
            fun getFactory(service: LobbyService, repo: AuthInfoRepo
            ) = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
                        LobbyViewModel(service = service, repo = repo) as T
                    }
                    else throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        private val _leaveLobbyState = MutableStateFlow<LeaveLobbyState>(LeaveLobbyState.Idle)
        val leaveLobbyState = _leaveLobbyState.asStateFlow()


        fun leaveLobby(user: User, lobbyId: Int) {
            viewModelScope.launch {
                try {
                    if (service.leaveLobby(user,lobbyId)) {
                        _leaveLobbyState.value = LeaveLobbyState.Success
                    } else {
                        throw Exception("Failed to leave lobby")
                    }
                } catch (e: Exception) {
                    _leaveLobbyState.value = LeaveLobbyState.Error(e)
                }
            }
        }

    }