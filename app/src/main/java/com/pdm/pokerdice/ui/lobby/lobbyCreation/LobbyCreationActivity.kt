package com.pdm.pokerdice.ui.lobby.lobbyCreation

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.ui.about.AboutActivity
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesActivity
import com.pdm.pokerdice.ui.lobby.lobbyIndividual.LobbyActivity
import com.pdm.pokerdice.ui.profile.ProfileActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.TitleScreenActions

class LobbyCreationActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LobbyCreationScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigate = { navigateTo(it) },
                        viewModel
                    )
                }
            }
        }
    }

    private val viewModel: LobbyCreationViewModel by viewModels {
        LobbyCreationViewModel.Companion.getFactory(
            service = (application as DependenciesContainer).lobbyService,
            repo = (application as DependenciesContainer).authInfoRepo
        )
    }
    private fun navigateTo(action: LobbyCreationNavigation) {
        val intent = when(action){
            is LobbyCreationNavigation.CreatedLobby -> Intent(this, LobbyActivity::class.java).apply {
                putExtra("lobby", action.lobby)
            }
        }
        startActivity(intent)
    }
}