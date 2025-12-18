package com.pdm.pokerdice.lobby

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
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.lobby.LobbyInfo
import kotlin.random.Random

const val PLAYER_MIN = 2
const val PLAYER_MAX = 10

@Composable
fun LobbyConfiguringView(
    onCreateLobby: (LobbyInfo) -> Unit
) {
    var minPlayers by remember { mutableIntStateOf(2) }
    var maxPlayers by remember { mutableIntStateOf(4) }
    var lobbyName by remember { mutableStateOf("") }
    var lobbyDescription by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Create New Lobby",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name
                OutlinedTextField(
                    value = lobbyName,
                    onValueChange = { lobbyName = it },
                    label = { Text("Lobby Name") },
                    placeholder = { Text("e.g., Royal Flush") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Description
                OutlinedTextField(
                    value = lobbyDescription,
                    onValueChange = { lobbyDescription = it },
                    label = { Text("Description") },
                    placeholder = { Text("e.g., Casual games") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Min Players
                PlayerCountSelector("Min Players", minPlayers, 
                    onDec = { if (minPlayers - 1 >= PLAYER_MIN) minPlayers-- },
                    onInc = { if (minPlayers + 1 <= maxPlayers) minPlayers++ }
                )

                // Max Players
                PlayerCountSelector("Max Players", maxPlayers,
                    onDec = { if (maxPlayers - 1 >= minPlayers) maxPlayers-- },
                    onInc = { if (maxPlayers + 1 <= PLAYER_MAX) maxPlayers++ }
                )
            }
            
            Button(
                onClick = {
                    val lobbyInfo = LobbyInfo(
                        name = lobbyName.normalizedLobbyName(),
                        description = lobbyDescription.trim(),
                        minPlayers = minPlayers,
                        maxPlayers = maxPlayers
                    )
                    onCreateLobby(lobbyInfo)
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Create Lobby")
            }
        }
    }
}

@Composable
fun PlayerCountSelector(label: String, count: Int, onDec: () -> Unit, onInc: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onDec) { Text("<", style = MaterialTheme.typography.titleLarge) }
            Text(text = "$count", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 24.dp))
            IconButton(onClick = onInc) { Text(">", style = MaterialTheme.typography.titleLarge) }
        }
    }
}

private fun String.normalizedLobbyName(): String {
    val raw = this.trim()
    val needsDefault = raw.isBlank()
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
