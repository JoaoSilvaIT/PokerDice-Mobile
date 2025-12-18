package com.pdm.pokerdice.lobby.lobbyIndividual

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
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.game.create.CreateGameActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.TitleActivity

class LobbyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lobby = intent.getParcelableExtra("lobby",
            Lobby::class.java) ?: Lobby(0, "Default Name", "Default Description",
            2, 4,
            mutableSetOf(),
            UserExternalInfo(0, "Default Host", 0))

        val user = intent.getParcelableExtra("user",
            UserExternalInfo::class.java) ?: UserExternalInfo(0, "Default User", 0)

        setContent {
            PokerDiceTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LobbyScreen(
                        Modifier.padding(innerPadding),
                        onNavigate = { handleNavigation(it) },
                        lobby = lobby,
                        user = user,
                        host = lobby.host,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private val viewModel: LobbyViewModel by viewModels {
        LobbyViewModel.getFactory(
            service = (application as DependenciesContainer).lobbyService,
            repo = (application as DependenciesContainer).authInfoRepo
        )
    }

    private fun handleNavigation(it: LobbyNavigation) {
        val intent = when (it) {
            LobbyNavigation.CreateGame -> Intent(this, CreateGameActivity::class.java)
            LobbyNavigation.TitleScreen -> Intent(this, TitleActivity::class.java)
        }
        startActivity(intent)
    }
}