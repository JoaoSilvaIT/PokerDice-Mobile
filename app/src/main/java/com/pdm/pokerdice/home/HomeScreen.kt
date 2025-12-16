package com.pdm.pokerdice.home

import androidx.compose.runtime.Composable
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navigator: HomeNavigation) {
    PokerDiceTheme {
        when (val currentState = viewModel.state) {
            is HomeScreenState.Success -> {
                HomeView(
                    creators = currentState.creators,
                    loginFunction = { navigator.goToLogin() },
                    signUpFunction = { navigator.goToSignup() }
                )
            }
        }
    }
}