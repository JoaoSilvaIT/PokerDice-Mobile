package com.pdm.pokerdice.ui.lobby.lobbies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.LocalAutofillHighlightColor
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import kotlinx.coroutines.launch

const val CREATE_LOBBY = "create_button"
sealed class LobbiesNavigation {
    class SelectLobby(val lobby: Lobby) : LobbiesNavigation()
    object CreateLobby : LobbiesNavigation()
}
@Composable
fun LobbiesScreen(
    modifier: Modifier,
    onNavigate: (LobbiesNavigation) -> Unit = {},
    viewModel: LobbiesViewModel
) {
    val currentJoinState by viewModel.joinLobbyState.collectAsState(JoinLobbyState.Idle)
    val lobbies by viewModel.lobbies.collectAsState()

    LaunchedEffect(currentJoinState) {
        if (currentJoinState is JoinLobbyState.Success) {
            val lobbyToJoin = (currentJoinState as JoinLobbyState.Success).lobby
            onNavigate(LobbiesNavigation.SelectLobby(lobbyToJoin))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Available Lobbies",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(lobbies) { lobby ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lobby.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = {
                                viewModel.joinLobby(lobby.lid)
                            },
                            modifier = Modifier.testTag("join_button_${lobby.lid}")

                        ) {
                            Text(
                                text = "Join Lobby"
                            )

                        }
                    }
                }
            }

        }
        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    val user = viewModel.getLoggedUser()
                    if (user != null) {
                        onNavigate(LobbiesNavigation.CreateLobby)
                    } else {
                        // Handle the case where the user is not logged in
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp)
                .testTag(CREATE_LOBBY),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Create Lobby")
        }
    }
}

/*
@Preview()
@Composable
fun LobbiesScreenPreview() {
    val repositoryLobby = RepoLobbyInMem()
    PokerDiceTheme {
        LobbiesScreen(User(1, "NULL", "null"),repositoryLobby, Modifier)
    }
}

 */
