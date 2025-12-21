package com.pdm.pokerdice.lobbies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.lobby.Lobby

@Composable
fun LobbiesView(
    lobbies: List<Lobby>,
    onJoinLobby: (lid: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Available Lobbies",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(16.dp),
    )

    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        items(lobbies) { lobby ->
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = lobby.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(
                        onClick = {
                            onJoinLobby(lobby.id)
                        },
                    ) {
                        Text(
                            text = "Join Lobby",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LobbiesScreenView(
    modifier: Modifier = Modifier,
    onJoinLobby: (lid: Int) -> Unit = {},
    onCreateLobby: () -> Unit = {},
    lobbies: List<Lobby>?,
    error: String? = null,
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            if (lobbies != null) {
                LobbiesView(
                    lobbies = lobbies,
                    onJoinLobby = onJoinLobby,
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = onCreateLobby,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(text = "Create Lobby")
            }
        }
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
            )
        }
    }
}

/*
@Preview
@Composable
fun LobbiesScreenViewPreview() {
    val sampleLobbies = listOf(
        Lobby(lid = 1,
            name = "Fun Lobby",
            description = "Test Lobby",
            users = listOf(User(
                uid = 1,
                name = "HostUser"
            )),
            expectedPlayers = 4,
            host = User(
                uid = 1,
                name = "HostUser"
            ),
            rounds = 1)
    )
    LobbiesScreenView(
        lobbies = sampleLobbies,
        {}
    )
}

 */
