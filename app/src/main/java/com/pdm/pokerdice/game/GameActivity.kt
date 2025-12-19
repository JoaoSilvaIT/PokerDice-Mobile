package com.pdm.pokerdice.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class GameActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context, gameId: Int) {
            val intent = Intent(context, GameActivity::class.java).apply {
                putExtra("gameId", gameId)
            }
            context.startActivity(intent)
        }
    }

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
                    viewModel = viewModel,
                    onBackIntent = { finish() }
                )
            }
        }
    }
}
