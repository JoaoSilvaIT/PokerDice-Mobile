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
import androidx.lifecycle.lifecycleScope
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.ui.about.AboutActivity
import com.pdm.pokerdice.ui.profile.ProfileActivity
import com.pdm.pokerdice.ui.authentication.signUp.SignUpActivity
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // For future use
        /*
        lifecycleScope.launch {
            val loggedIn =
                checkLoggedUser((application as com.pdm.pokerdice.PokerDice).authInfoRepo)
            if (loggedIn) {
                startActivity(Intent(this@MainActivity, LobbiesActivity::class.java))
                finish()
            } else {
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
        }

         */

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
                TitleScreenActions.SignUp -> Intent(this, SignUpActivity::class.java)
            }
        startActivity(intent)
    }

    // For future use
    private suspend fun checkLoggedUser(repo : AuthInfoRepo): Boolean {
        val loggedUser = repo.getAuthInfo()
        return loggedUser != null
    }
}