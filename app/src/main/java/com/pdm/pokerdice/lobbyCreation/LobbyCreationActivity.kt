package com.pdm.pokerdice.lobbyCreation

import android.content.Intent
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

class LobbyCreationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LobbyCreationScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { navigateTo(it) },
                        viewModel,
                    )
                }
            }
        }
    }

    private val viewModel: LobbyCreationViewModel by viewModels {
        LobbyCreationViewModel.Companion.getFactory(
            service = (application as DependenciesContainer).lobbyService,
            repo = (application as DependenciesContainer).authInfoRepo,
        )
    }

    private fun navigateTo(action: LobbyCreationNavigation) {
        val intent =
            when (action) {
                is LobbyCreationNavigation.CreatedLobby ->
                    Intent(this, LobbyActivity::class.java).apply {
                        putExtra("lobby", action.lobby)
                        putExtra("user", action.lobby.host)
                    }
            }
        startActivity(intent)
    }
}
