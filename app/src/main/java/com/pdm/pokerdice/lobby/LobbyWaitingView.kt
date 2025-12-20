package com.pdm.pokerdice.lobby

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
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo

@Composable
fun LobbyWaitingView(
    lobby: Lobby,
    currentUser: UserExternalInfo,
    onStartGame: () -> Unit,
    onLeaveLobby: () -> Unit
) {
    val host = lobby.host

    Column(
        modifier = Modifier
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
        
        Text(
            text = "Players (${lobby.players.size}/${lobby.settings.maxPlayers})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(lobby.players.toList()) { player ->
                PlayerItem(player, currentUser, host)
            }
        }

        // Only Host can start match
        val isHost = currentUser.id == host.id
        val canStart = lobby.players.size >= 2
        
        if (isHost) {
            Button(
                onClick = onStartGame,
                enabled = canStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                val text = if (canStart) "Start Match" else "Wait for Players"
                Text(text)
            }
            
            if (!canStart) {
                Text(
                    text = "You need at least 2 players to start a match",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        } else {
            Text(
                "Waiting for host to start...",
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = onLeaveLobby,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Leave Lobby")
        }
    }
}

@Composable
fun PlayerItem(player: UserExternalInfo, currentUser: UserExternalInfo, host: UserExternalInfo) {
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
            val isMe = player.id == currentUser.id
            val isHost = player.id == host.id
            
            val text = buildString {
                append(player.name)
                if (isHost) append(" (Host)")
                if (isMe) append(" (You)")
            }
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isMe) MaterialTheme.colorScheme.primary else if (isHost) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
