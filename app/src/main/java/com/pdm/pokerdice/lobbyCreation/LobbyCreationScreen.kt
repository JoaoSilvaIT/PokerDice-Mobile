package com.pdm.pokerdice.lobbyCreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbyInfo
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroGold
import com.pdm.pokerdice.ui.theme.RetroNavyDark
import com.pdm.pokerdice.ui.theme.RetroTeal
import com.pdm.pokerdice.ui.theme.RetroTextDark

const val PLAYER_MIN = 2
const val PLAYER_MAX = 10

const val MAX_ROUNDS = 60

sealed class LobbyCreationNavigation {
    class CreatedLobby(
        val lobby: Lobby,
    ) : LobbyCreationNavigation()
}

const val DESCRIPTION = "lobby_description"
const val LOBBY_NAME = "lobby_name"

const val INCREMENT_LIMIT = "increment_player_limit"
const val DECREMENT_LIMIT = "decrement_player_limit"

const val CREATE_LOBBY = "create_lobby_button"
const val INCREMENT_ROUNDS = "increment_rounds"
const val DECREMENT_ROUNDS = "decrement_rounds"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyCreationScreen(
    modifier: Modifier,
    onNavigate: (LobbyCreationNavigation) -> Unit = {},
    viewModel: LobbyCreationViewModel,
) {
    var minPlayers by remember { mutableIntStateOf(2) }
    var maxPlayers by remember { mutableIntStateOf(4) }
    var lobbyName by remember { mutableStateOf("") }
    var lobbyDescription by remember { mutableStateOf("") }
    val currentCreationState by viewModel.createLobbyState.collectAsState(LobbyCreationState.Idle)

    LaunchedEffect(currentCreationState) {
        if (currentCreationState is LobbyCreationState.Success) {
            val createdLobby = (currentCreationState as LobbyCreationState.Success).lobby
            onNavigate(LobbyCreationNavigation.CreatedLobby(createdLobby))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = "Create Lobby",
                // No back arrow requested
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroGold,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = RetroNavyDark,
                        cursorColor = RetroTeal,
                        focusedTextColor = RetroTextDark,
                        unfocusedTextColor = RetroTextDark,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = lobbyName,
                            onValueChange = { lobbyName = it },
                            label = { Text("Lobby Name", style = MaterialTheme.typography.labelLarge) },
                            placeholder = { Text("e.g., Royal Flush", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier.testTag(LOBBY_NAME),
                            shape = MaterialTheme.shapes.small,
                            colors = textFieldColors
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = lobbyDescription,
                            onValueChange = { lobbyDescription = it },
                            label = { Text("Description", style = MaterialTheme.typography.labelLarge) },
                            placeholder = { Text("e.g., Casual games, 10 rounds", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier.testTag(DESCRIPTION),
                            shape = MaterialTheme.shapes.small,
                            colors = textFieldColors
                        )
                    }
                    Text(
                        text = "Min Players",
                        style = MaterialTheme.typography.labelLarge,
                        color = RetroTextDark,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(
                            enabled = (minPlayers - 1) >= PLAYER_MIN,
                            onClick = { minPlayers-- },
                            modifier = Modifier.testTag(DECREMENT_LIMIT),
                        ) {
                            Text("<", style = MaterialTheme.typography.titleLarge, color = RetroNavyDark)
                        }
                        Text(
                            text = "$minPlayers",
                            style = MaterialTheme.typography.titleLarge,
                            color = RetroNavyDark,
                            modifier = Modifier.padding(horizontal = 24.dp),
                        )
                        IconButton(
                            enabled = (minPlayers + 1) <= maxPlayers,
                            onClick = { minPlayers++ },
                            modifier = Modifier.testTag(INCREMENT_LIMIT),
                        ) {
                            Text(">", style = MaterialTheme.typography.titleLarge, color = RetroNavyDark)
                        }
                    }
                    Text(
                        text = "Max Players",
                        style = MaterialTheme.typography.labelLarge,
                        color = RetroTextDark,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(
                            enabled = (maxPlayers - 1) >= minPlayers,
                            onClick = { maxPlayers-- },
                            modifier = Modifier.testTag(DECREMENT_ROUNDS),
                        ) {
                            Text("<", style = MaterialTheme.typography.titleLarge, color = RetroNavyDark)
                        }
                        Text(
                            text = "$maxPlayers",
                            style = MaterialTheme.typography.titleLarge,
                            color = RetroNavyDark,
                            modifier = Modifier.padding(horizontal = 24.dp),
                        )
                        IconButton(
                            enabled = (maxPlayers + 1) <= PLAYER_MAX,
                            onClick = { maxPlayers++ },
                            modifier = Modifier.testTag(INCREMENT_ROUNDS),
                        ) {
                            Text(">", style = MaterialTheme.typography.titleLarge, color = RetroNavyDark)
                        }
                    }
                }
                Button(
                    onClick = {
                        val lobbyInfo =
                            LobbyInfo(
                                name = lobbyName,
                                description = lobbyDescription.trim(),
                                minPlayers = minPlayers,
                                maxPlayers = maxPlayers,
                            )
                        viewModel.createLobby(lobbyInfo)
                    },
                    modifier = Modifier
                        .testTag(CREATE_LOBBY)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = RetroNavyDark
                    )
                ) {
                    Text("Create Lobby", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}
