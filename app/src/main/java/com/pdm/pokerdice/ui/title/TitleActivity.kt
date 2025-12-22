package com.pdm.pokerdice.ui.title

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.about.AboutActivity
import com.pdm.pokerdice.lobbies.LobbiesActivity
import com.pdm.pokerdice.profile.ProfileActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class TitleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerDiceTheme {
                TitleScreen(
                    modifier = Modifier,
                    onNavigate = { navigateTo(it) },
                )
            }
        }
    }

    private fun navigateTo(action: TitleScreenActions) {
        val intent =
            when (action) {
                TitleScreenActions.About -> Intent(this, AboutActivity::class.java)
                TitleScreenActions.Profile -> Intent(this, ProfileActivity::class.java)
                TitleScreenActions.Lobbies -> Intent(this, LobbiesActivity::class.java)
            }
        startActivity(intent)
    }
}
