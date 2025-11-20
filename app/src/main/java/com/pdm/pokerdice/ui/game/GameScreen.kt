package com.pdm.pokerdice.ui.game

import androidx.compose.ui.Modifier
sealed class GameNavigation {
    object TitleScreen : GameNavigation()
}

fun GameScreen(
    modifier: Modifier,
    onNavigate: (GameNavigation) -> Unit = {},
    viewModel: GameViewModel
){

}