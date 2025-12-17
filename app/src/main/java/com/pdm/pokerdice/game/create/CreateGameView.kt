// kotlin
package com.pdm.pokerdice.game.create

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


sealed class CreateGameNavigation {
    data class RoundsSelected(val rounds: Int) : CreateGameNavigation()
}
@Composable
fun CreateGameView(
    modifier: Modifier = Modifier,
    numberOfPlayers: Int = 2,
    onNavigate: (CreateGameNavigation) -> Unit = {},
) {
    var rounds by remember { mutableIntStateOf(numberOfPlayers) }

    fun increase() {
        val next = rounds + numberOfPlayers
        rounds = next
    }

    fun decrease() {
        val prev = rounds - numberOfPlayers
        rounds = prev
    }

    val isValid = rounds % numberOfPlayers == 0 && rounds <= 60

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose the number of the rounds of the game",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { decrease() },
                enabled = (rounds - numberOfPlayers) >= numberOfPlayers
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Decrease")
            }
            Text(
                text = rounds.toString(),
                style = MaterialTheme.typography.headlineMedium,
            )

            IconButton(
                onClick = { increase() },
                enabled = (rounds + numberOfPlayers) <= 60
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase")
            }
        }


        Text(
            text = "The number of rounds must be a multiple of the number of players and less than 60.",
            color = Color.Red
        )


        Button(
            onClick = { onNavigate(CreateGameNavigation.RoundsSelected(rounds)) },
            enabled = isValid,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Start Game")
        }
    }
}

@Preview
@Composable
fun CreateGameViewPreview() {
    CreateGameView(numberOfPlayers = 3)
}
