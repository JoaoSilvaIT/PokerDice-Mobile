package com.pdm.pokerdice.ui.lobby.lobbyCreation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.users1st
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesNavigation

const val PLAYER_MIN = 2
const val PLAYER_MAX = 10

sealed class LobbyCreationNavigation() {

    class CreatedLobby(val lobby: Lobby) : LobbyCreationNavigation()
}

const val DESCRIPTION = "lobby_description"
const val LOBBY_NAME = "lobby_name"

const val INCREMENT_LIMIT = "increment_player_limit"
const val DECREMENT_LIMIT = "decrement_player_limit"

const val CREATE_LOBBY = "create_lobby_button"
const val INCREMENT_ROUNDS = "increment_rounds"
const val DECREMENT_ROUNDS = "decrement_rounds"
@Composable
fun LobbyCreationScreen(modifier: Modifier, onNavigate: (LobbyCreationNavigation) -> Unit = {}, user: User) {
    var playerLimit by remember { mutableStateOf(2) }
    var lobbyName by remember { mutableStateOf("") }
    var lobbyInfo by remember { mutableStateOf("") }
    var rounds by remember { mutableStateOf(playerLimit) }
    var maxPlayers by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = lobbyName,
                        onValueChange = { lobbyName = it },
                        label = { Text("Lobby Name") },
                        modifier = Modifier.testTag(LOBBY_NAME)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    OutlinedTextField(
                        value = lobbyInfo,
                        onValueChange = { lobbyInfo = it },
                        label = { Text("Description") },
                        modifier = Modifier.testTag(DESCRIPTION)
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = maxPlayers,
                        onValueChange = { newValue ->
                            // Filtra apenas números
                            if (newValue.all { it.isDigit() }) {
                                maxPlayers = newValue
                            }
                        },
                        label = { Text("Número Máximo de Players") }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = playerLimit > PLAYER_MIN,
                        onClick = { playerLimit-- },
                        modifier = Modifier.testTag(DECREMENT_LIMIT)
                    ) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        text = "$playerLimit",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    IconButton(
                        enabled = playerLimit < PLAYER_MAX,
                        onClick = { playerLimit++ },
                        modifier = Modifier.testTag(INCREMENT_LIMIT)
                    ) {
                        Text(">", style = MaterialTheme.typography.titleLarge)
                    }
                }
                Text(
                    text = "Rounds",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    IconButton(
                        enabled = rounds >= playerLimit,
                        onClick = { rounds-- },
                        modifier = Modifier.testTag(DECREMENT_ROUNDS)
                    ) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        text = "$rounds",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    IconButton(
                        enabled = rounds <= playerLimit,
                        onClick = { rounds++ },
                        modifier = Modifier.testTag(INCREMENT_ROUNDS)
                    ) {
                        Text(">", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
            Button(onClick = {onNavigate(LobbyCreationNavigation.CreatedLobby(Lobby(4, lobbyName, lobbyInfo,
                listOf(user), maxPlayers.toInt(), user)))},
                modifier = Modifier.testTag(CREATE_LOBBY)
            ) {
                Text("Create Lobby")
            }
        }
    }
}


@Preview
@Composable
fun LobbyCreationPreview() {
    LobbyCreationScreen(Modifier.fillMaxSize(), user = User(1, "null", "null")) // Mock User
}