package com.pdm.pokerdice.lobby

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo

sealed class LobbyNavigation {
    class CreateGame(val game: Game): LobbyNavigation()
    object TitleScreen : LobbyNavigation()
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun LobbyScreen(
    modifier: Modifier = Modifier,
    onNavigate: (LobbyNavigation) -> Unit = {},
    lobby: Lobby,
    user: UserExternalInfo,
    host: UserExternalInfo,
    viewModel: LobbyViewModel
) {
    val currentLeaveState by viewModel.leaveLobbyState.collectAsState(LeaveLobbyState.Idle)
    val currentCreateGameState by viewModel.createGameState.collectAsState(CreateGameState.Idle)

    LaunchedEffect(currentLeaveState) {
        if (currentLeaveState is LeaveLobbyState.Success) {
            onNavigate(LobbyNavigation.TitleScreen)
        }
    }

    LaunchedEffect(currentCreateGameState) {
        if (currentCreateGameState is CreateGameState.Success) {
            onNavigate(LobbyNavigation.CreateGame((currentCreateGameState as CreateGameState.Success).game))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = lobby.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = lobby.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // List of Players
        Text(
            text = "Players in Lobby:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f) // ocupa o resto do espaço
        ) {
            items(lobby.players.toList()) { usr ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (usr.name == user.name) {
                            if (usr.name == host.name) {
                                Text(
                                    text = "${user.name} (Host, You)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else
                                Text(
                                    text = "${user.name} (You)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                        } else {
                            if (usr.name == host.name) {
                                Text(
                                    text = "${usr.name} (Host)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            } else
                                Text(text = usr.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }

        if (user.id == host.id) {
            Button(
                onClick = { viewModel.createGame(lobby, 2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Start Game 🎮", style = MaterialTheme.typography.titleSmall)
            }
        }

        Button(
            onClick = {
                viewModel.leaveLobby(user, lobby.id)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Leave Lobby", style = MaterialTheme.typography.titleSmall)
        }
    }
}

/*
@Preview
@Composable
fun LobbyScreenPreview(){
    PokerDiceTheme {
        LobbyScreen(
            Modifier, lobby = Lobby(
                1,
                "Test Lobby",
                "This is a test lobby description",
                mutableListOf(),
                10,
                User(1, "null", "null"),
                10
            )
        )
    }
}

 */