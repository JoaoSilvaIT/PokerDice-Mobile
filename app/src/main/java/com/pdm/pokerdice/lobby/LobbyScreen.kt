package com.pdm.pokerdice.lobby

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.ui.theme.GenericTopAppBar

sealed class LobbyNavigation {
    data class StartGame(val gameId: Int) : LobbyNavigation()
    object Back : LobbyNavigation()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    viewModel: LobbyViewModel,
    onNavigate: (LobbyNavigation) -> Unit
) {
    val state = viewModel.screenState

    LaunchedEffect(state) {
        if (state is LobbyScreenState.GameStarted) {
            onNavigate(LobbyNavigation.StartGame(state.gameId))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = when(state) {
                    is LobbyScreenState.Configuring -> "Create Lobby"
                    is LobbyScreenState.Waiting -> "Lobby: ${state.lobby.name}"
                    else -> "Lobby"
                },
                onBackAction = { 
                    // If configuring, back exits activity. If waiting, leave lobby?
                    // For simplicity, let's say Back button on TopBar always navigates Back/Exit
                    onNavigate(LobbyNavigation.Back)
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (state) {
                is LobbyScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LobbyScreenState.Configuring -> {
                    LobbyConfiguringView(
                        onCreateLobby = { info -> viewModel.createLobby(info) }
                    )
                }
                is LobbyScreenState.Waiting -> {
                    LobbyWaitingView(
                        lobby = state.lobby,
                        currentUser = state.user,
                        onStartGame = { viewModel.startGame() },
                        onLeaveLobby = { 
                            viewModel.leaveLobby()
                            onNavigate(LobbyNavigation.Back)
                        }
                    )
                }
                is LobbyScreenState.GameStarted -> {
                    // Show loading while navigating
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LobbyScreenState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
