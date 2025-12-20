package com.pdm.pokerdice.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.ui.theme.GenericTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackIntent: () -> Unit
) {
    val state = viewModel.screenState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = "Poker Dice Match",
                onBackAction = onBackIntent
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is GameScreenState.Loading -> Text("Loading Game...")
                is GameScreenState.SettingAnte -> SetAnteView(
                    isMyTurn = state.isMyTurn,
                    onConfirm = { viewModel.submitAnte(it) }
                )
                is GameScreenState.Playing -> PlayingView(
                    state = state,
                    onRollClick = { viewModel.rollDice() },
                    onHoldClick = { viewModel.holdDice() },
                    onDieClick = { viewModel.toggleHoldDie(it) },
                    onEndTurnClick = { viewModel.endTurn() }
                )
                is GameScreenState.RoundEndedStub -> Text("Round Ended")
                is GameScreenState.MatchEnded -> MatchEndedView(
                    state = state,
                    onExitClick = onBackIntent
                )
                is GameScreenState.Error -> Text("Error: ${state.message}")
            }

            // Notification Overlay for Round Winner
            viewModel.lastWinnerNotification?.let { message ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}