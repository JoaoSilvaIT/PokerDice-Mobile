package com.pdm.pokerdice.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbyCountdown

/**
 * Composable to display countdown timer when lobby reaches min players
 *
 * Usage in your LobbyScreen.kt:
 * ```
 * val countdown by viewModel.countdown.collectAsState()
 * val gameStarted by viewModel.gameStarted.collectAsState()
 *
 * // Start monitoring when entering lobby
 * LaunchedEffect(lobbyId) {
 *     viewModel.startMonitoringLobby(lobbyId)
 * }
 *
 * // Navigate to game when started
 * LaunchedEffect(gameStarted) {
 *     gameStarted?.let { (lobbyId, gameId) ->
 *         navigateToGame(gameId)
 *     }
 * }
 *
 * // Show countdown in UI
 * countdown?.let { CountdownDisplay(it) }
 * ```
 */
@Composable
fun CountdownDisplay(
    countdown: LobbyCountdown,
    modifier: Modifier = Modifier,
) {
    // Debug: Calculate what the time SHOULD be
    val now = System.currentTimeMillis()
    val actualRemaining = ((countdown.expiresAt - now) / 1000).coerceAtLeast(0)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Game Starting In",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatCountdown(countdown.remainingSeconds),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            // Debug info - remove this later
            if (countdown.remainingSeconds != actualRemaining) {
                Text(
                    text = "DEBUG: Should be ${actualRemaining}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { calculateProgress(countdown) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Get ready!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            )
        }
    }
}

private fun formatCountdown(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) {
        String.format("%d:%02d", mins, secs)
    } else {
        seconds.toString()
    }
}

private fun calculateProgress(countdown: LobbyCountdown): Float {
    // Calculate total countdown duration from expiresAt timestamp
    val now = System.currentTimeMillis()
    val totalDuration = countdown.expiresAt - (now - countdown.remainingSeconds * 1000)
    val totalSeconds = totalDuration / 1000

    return if (totalSeconds > 0) {
        (countdown.remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
}

/**
 * Display lobby players with countdown (if active)
 */
@Composable
fun LobbyWithCountdown(
    lobby: Lobby,
    countdown: LobbyCountdown?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Your existing lobby UI here
        Text("Lobby: ${lobby.name}")
        Text("Players: ${lobby.players.size}/${lobby.settings.maxPlayers}")

        Spacer(modifier = Modifier.height(16.dp))

        // Show countdown when it starts
        countdown?.let {
            CountdownDisplay(countdown = it)
        }
    }
}
