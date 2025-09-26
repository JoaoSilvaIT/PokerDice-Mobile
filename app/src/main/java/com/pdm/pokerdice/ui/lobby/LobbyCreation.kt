package com.pdm.pokerdice.ui.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val PLAYER_MIN = 2
const val PLAYER_MAX = 10

@Composable
fun LobbyCreationScreen(modifier: Modifier){
    var playerLimit by remember { mutableStateOf(2) }
    var isPrivate by remember { mutableStateOf(false) }

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
                    Text("Private lobby", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = playerLimit > PLAYER_MIN,
                        onClick = { playerLimit-- }
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
                        onClick = { playerLimit++ }
                    ) {
                        Text(">", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LobbyCreationPreview() {
    LobbyCreationScreen(Modifier.fillMaxSize())
}