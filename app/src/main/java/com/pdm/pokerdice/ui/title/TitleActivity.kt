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
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.ui.about.AboutActivity
import com.pdm.pokerdice.ui.authentication.login.LoginActivity
import com.pdm.pokerdice.ui.profile.ProfileActivity
import com.pdm.pokerdice.ui.authentication.signUp.SignUpActivity
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class TitleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    TitleScreen(
                        modifier = Modifier.Companion.padding(innerPadding),
                        onNavigate = { navigateTo(it) },
                    )
                }
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