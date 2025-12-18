package com.pdm.pokerdice.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class GameActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, GameActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: GameViewModel by viewModels {
        GameViewModel.getFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
