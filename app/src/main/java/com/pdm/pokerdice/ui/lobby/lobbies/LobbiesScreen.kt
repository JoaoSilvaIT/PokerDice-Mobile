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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.ui.lobby.lobbyCreation.LobbyCreationNavigation
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

const val CREATE_LOBBY = "create_button"
sealed class LobbiesNavigation {
    class SelectLobby(lobby: Lobby = Lobby(0, "Default Name", "Default Description")) : LobbiesNavigation()
    object CreatLobby : LobbiesNavigation()
}
@Composable
fun LobbiesScreen(
    mod: Modifier = Modifier,
    onNavigate: (LobbiesNavigation) -> Unit = {}
) {

    //Vai ser mudado, só para ficar de exemplo
    val lobbies = listOf(
        Lobby(1, "High Rollers", ""),
        Lobby(2, "Casual Dice", ""),
        Lobby(3, "Late Night Lobby", ""),
        Lobby(4, "Fast Game", "")
    )

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
                            onClick = { onNavigate(LobbiesNavigation.SelectLobby(lobby)) }

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
            onClick = { onNavigate(LobbiesNavigation.CreatLobby) },
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

@Preview()
@Composable
fun LobbiesScreenPreview() {
    PokerDiceTheme {
        LobbiesScreen(Modifier)
    }
}
