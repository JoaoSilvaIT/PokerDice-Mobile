package com.pdm.pokerdice.home

import androidx.compose.runtime.Composable
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, onNavigate: (HomeNavigation) -> Unit = {}) {
    when (val currentState = viewModel.currentState) {
        is HomeScreenState.Success -> {
            HomeView(
                creators = currentState.creators,
                onNavigate
            )
        }
    }
}