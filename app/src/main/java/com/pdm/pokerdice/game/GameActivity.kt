package com.pdm.pokerdice.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.domain.game.utilis.State

class GameActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        GameViewModel.getFactory(
            service = (application as DependenciesContainer).gameService,
            authRepo = (application as DependenciesContainer).authInfoRepo
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // Inicializar o ViewModel com o gameId do game recebido
        if (game.id != -1) {
            viewModel.initialize(game.id)
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
