package com.pdm.pokerdice.ui.lobby.lobbies

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.ui.lobby.lobbyCreation.LobbyCreationActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class LobbiesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerDiceTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LobbiesScreen(
                        Modifier.padding(innerPadding), onNavigate = { handleNavigation(it) }
                    )
                }
            }
        }
    }

    private fun handleNavigation(it: LobbiesNavigation) {
       val intent = when (it) {
                LobbiesNavigation.CreatLobby -> Intent(this, LobbyCreationActivity::class.java)
                LobbiesNavigation.SelectLobby -> TODO()
       }
        startActivity(intent)
    }
}