package com.pdm.pokerdice.lobbies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo

/**
 * Represents the possible navigation destinations of the lobbies screen.
 */
sealed class LobbiesNavigation {
    class SelectLobby(val lobby: Lobby, val user: UserExternalInfo) : LobbiesNavigation()
    object CreateLobby : LobbiesNavigation()
}

@Composable
fun LobbiesScreen(
    modifier: Modifier = Modifier,
    onNavigate: (LobbiesNavigation) -> Unit = {},
    viewModel: LobbiesViewModel
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is LobbiesScreenState.Loading -> {
            LobbiesScreenView(
                lobbies = null,
                modifier = modifier
            )
        }
        is LobbiesScreenState.ViewLobbies -> {
            LobbiesScreenView(
                lobbies = currentState.lobbies,
                onJoinLobby = { lobbyId -> viewModel.joinLobby(lobbyId) },
                onCreateLobby = { onNavigate(LobbiesNavigation.CreateLobby) },
                modifier = modifier
            )
        }
        is LobbiesScreenState.JoinLobby -> {
            // Use LaunchedEffect to ensure navigation happens only once
            LaunchedEffect(currentState) {
                onNavigate(LobbiesNavigation.SelectLobby(currentState.lobby, currentState.user))
                viewModel.resetToIdle() // Reset state so we don't re-navigate on return
            }
        }
        is LobbiesScreenState.Error -> {
            LobbiesScreenView(
                lobbies = null,
                error = currentState.message,
                modifier = modifier
            )
        }
    }
}