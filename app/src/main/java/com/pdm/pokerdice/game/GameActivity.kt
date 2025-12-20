package com.pdm.pokerdice.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.domain.game.utilis.State

class GameActivity : ComponentActivity() {

    val game = intent.getParcelableExtra<Game>(
        "game", Game::class.java
    ) ?: Game(
        id = -1,
        lobbyId = null,
        players = emptyList(),
        numberOfRounds = 5,
        state = State.WAITING,
        currentRound = null,
        startedAt = System.currentTimeMillis(),
        endedAt = null,
        gameGains = emptyList()
    )
    private val viewModel: GameViewModel by viewModels {
        GameViewModel.getFactory(
            service = (application as DependenciesContainer).gameService,
            authRepo = (application as DependenciesContainer).authInfoRepo
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val gameId = intent.getIntExtra("gameId", -1)
        if (gameId != -1) {
            viewModel.initialize(gameId)
        }

        setContent {
            PokerDiceTheme {
                GameScreen(
                    game = game,
                    viewModel = viewModel,
                    onBackIntent = { finish() }
                )
            }
        }
    }
}
