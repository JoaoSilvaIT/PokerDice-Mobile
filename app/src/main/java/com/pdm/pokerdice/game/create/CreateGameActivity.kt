package com.pdm.pokerdice.game.create

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class CreateGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    CreateGameView(
                        modifier = Modifier.Companion.padding(innerPadding)
                    )
                }
            }
        }
    }

    private val viewModel: CreateGameViewModel by viewModels {
        CreateGameViewModel.Companion.getFactory(
            service = (application as DependenciesContainer).gameService,
        )
    }
    /*
    private fun handleNavigation(it: GameNavigation) {
        val intent = when (it) {
            GameNavigation.TitleScreen -> null
        }
    }

     */
}