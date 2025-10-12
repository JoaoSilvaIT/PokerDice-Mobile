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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import kotlin.random.Random

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
    var maxPlayers by remember { mutableIntStateOf(2) }
    var lobbyName by remember { mutableStateOf("") }
    var lobbyInfo by remember { mutableStateOf("") }
    var rounds by remember { mutableIntStateOf(maxPlayers) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
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
                        label = { Text("Lobby Name", style = MaterialTheme.typography.labelLarge) },
                        placeholder = { Text("e.g., Royal Flush", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.testTag(LOBBY_NAME),
                        shape = MaterialTheme.shapes.small
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
                        label = { Text("Description", style = MaterialTheme.typography.labelLarge) },
                        placeholder = { Text("e.g., Casual games, 10 rounds", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.testTag(DESCRIPTION),
                        shape = MaterialTheme.shapes.small
                    )
                }
                Text(
                    text = "Max Players",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = maxPlayers > PLAYER_MIN,
                        onClick = { maxPlayers-- },
                        modifier = Modifier.testTag(DECREMENT_LIMIT)
                    ) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        text = "$maxPlayers",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    IconButton(
                        enabled = maxPlayers < PLAYER_MAX,
                        onClick = { maxPlayers++ },
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
                        enabled = rounds >= maxPlayers,
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
                        enabled = rounds <= maxPlayers,
                        onClick = { rounds++ },
                        modifier = Modifier.testTag(INCREMENT_ROUNDS)
                    ) {
                        Text(">", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
            Button(
                onClick = {
                    val normalizedName = lobbyName.normalizedLobbyName()
                    onNavigate(
                        LobbyCreationNavigation.CreatedLobby(
                            Lobby(
                                4,
                                normalizedName,
                                lobbyInfo.trim(),
                                listOf(user),
                                maxPlayers,
                                user,
                                rounds
                            )
                        )
                    )
                },
                modifier = Modifier.testTag(CREATE_LOBBY),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Create Lobby", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

private fun String.normalizedLobbyName(): String {
    val raw = this.trim()
    val needsDefault = raw.isBlank() || raw.equals("LobbyTest", ignoreCase = true) || raw.equals("Lobby", ignoreCase = true)
    val base = if (needsDefault) generateFriendlyLobbyName() else raw
    return base.split(Regex("\\s+")).joinToString(" ") { part ->
        part.lowercase().replaceFirstChar { c -> c.titlecase() }
    }
}

private fun generateFriendlyLobbyName(): String {
    val adjectives = listOf("Royal", "Lucky", "Golden", "Wild", "Rolling", "Crimson", "Silver")
    val nouns = listOf("Flush", "Aces", "Dice", "Hands", "Sevens", "Stacks", "Pairs")
    val number = Random.nextInt(100, 999)
    return "${adjectives.random()} ${nouns.random()} #$number"
}

@Preview
@Composable
fun LobbyCreationPreview() {
    LobbyCreationScreen(Modifier.fillMaxSize(), user = User(1, "null", "null")) // Mock User
}