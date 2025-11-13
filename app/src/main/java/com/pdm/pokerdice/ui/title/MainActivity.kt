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
import com.pdm.pokerdice.repo.mem.RepoUserInMem
import com.pdm.pokerdice.ui.about.AboutActivity
import com.pdm.pokerdice.ui.profile.ProfileActivity
import com.pdm.pokerdice.ui.signUp.SignUpActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class MainActivity : ComponentActivity() {
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

    val repoUser = RepoUserInMem()

    private fun navigateTo(action: TitleScreenActions) {
        val intent =
            when (action) {
                TitleScreenActions.About -> Intent(this, AboutActivity::class.java)
                TitleScreenActions.Profile -> Intent(this, ProfileActivity::class.java)
                TitleScreenActions.SignUp -> Intent(this, SignUpActivity::class.java)
            }
        startActivity(intent)
    }
}