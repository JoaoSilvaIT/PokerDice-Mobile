package com.pdm.pokerdice.ui.lobby.lobbyIndividual

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesNavigation
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

sealed class LobbyNavigation {
    object GameLobby : LobbyNavigation()
}

@Composable
fun LobbyScreen(
    mod: Modifier = Modifier,
    onNavigate: (LobbyNavigation) -> Unit = {},
    lobby: Lobby = Lobby(0, "Default Name", "Default Description"),
    users: List<User> = emptyList<User>()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = lobby.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun LobbyScreenPreview(){
    PokerDiceTheme {
        LobbyScreen(Modifier)
    }
}