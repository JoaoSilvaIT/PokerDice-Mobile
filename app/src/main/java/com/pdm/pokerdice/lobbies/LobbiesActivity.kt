package com.pdm.pokerdice.lobbies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.lobby.LobbyActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class LobbiesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->
                    LobbiesScreen(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel,
                        onNavigate = { handleNavigation(it) }
                    )
                }
            }
        }
    }

    private val viewModel: LobbiesViewModel by viewModels {
        LobbiesViewModel.getFactory(
            service = (application as DependenciesContainer).lobbyService,
        )
    }

    private fun handleNavigation(it: LobbiesNavigation) {
       when (it) {
           is LobbiesNavigation.CreateLobby -> {
               // Navigate to LobbyActivity without args -> Configuring mode
               LobbyActivity.navigate(this)
           }

           is LobbiesNavigation.SelectLobby -> {
               // Navigate to LobbyActivity with lobby/user -> Waiting mode
               LobbyActivity.navigate(this, it.lobby, it.user)
           }
       }
    }
}
