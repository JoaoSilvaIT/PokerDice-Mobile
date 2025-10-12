package com.pdm.pokerdice.ui.lobby.lobbyIndividual

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

sealed class LobbyNavigation {
    object GameLobby : LobbyNavigation()

    object TitleScreen : LobbyNavigation()
}

@Composable
fun LobbyScreen(
    modifier: Modifier = Modifier,
    onNavigate: (LobbyNavigation) -> Unit = {},
    lobby: Lobby
) {
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
            items(lobby.users) { user ->
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
                        Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        // Start Match button (navigate)
        Button(
            onClick = { }, // In the future will be used to start a game
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Start Match", style = MaterialTheme.typography.titleSmall)
        }

        Button(
            onClick = { onNavigate(LobbyNavigation.TitleScreen) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Leave Lobby", style = MaterialTheme.typography.titleSmall)
        }
    }
}

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