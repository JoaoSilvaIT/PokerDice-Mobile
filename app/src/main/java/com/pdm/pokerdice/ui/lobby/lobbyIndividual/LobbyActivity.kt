package com.pdm.pokerdice.ui.lobby.lobbyIndividual

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesActivity
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesScreen
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.MainActivity
import com.pdm.pokerdice.ui.title.TitleScreen

class LobbyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lobby = intent.getParcelableExtra("lobby",
            Lobby::class.java) ?: Lobby(0, "Default Name", "Default Description",
            mutableListOf(),
            10,
                User(0, "Default User", ""),
                2)

        setContent {
            PokerDiceTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LobbyScreen(
                        Modifier.padding(innerPadding),
                        onNavigate = { handleNavigation(it) },
                        lobby = lobby
                    )
                }
            }
        }
    }

    private fun handleNavigation(it: LobbyNavigation) {
        val intent = when (it) {
            LobbyNavigation.GameLobby -> TODO()
            LobbyNavigation.TitleScreen -> Intent(this, MainActivity::class.java)
        }

        startActivity(intent)
    }
}