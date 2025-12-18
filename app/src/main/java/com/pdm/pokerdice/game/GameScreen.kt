package com.pdm.pokerdice.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chimp.ui.common.GenericTopAppBar

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
                onBackAction = onBackIntent // Usually disabled during match, but good for debug
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
                    onConfirm = { viewModel.submitAnte(it) }
                )
                is GameScreenState.Playing -> PlayingView(
                    state = state,
                    onRollClick = { viewModel.rollDice() },
                    onDieClick = { viewModel.toggleHoldDie(it) },
                    onEndTurnClick = { viewModel.endTurn() }
                )
                is GameScreenState.RoundEnded -> RoundEndedView(
                    state = state,
                    onNextRoundClick = { viewModel.nextRound() }
                )
                is GameScreenState.MatchEnded -> MatchEndedView(
                    state = state,
                    onExitClick = onBackIntent
                )
            }
        }
    }
}