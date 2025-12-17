package com.pdm.pokerdice.signUp

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
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.lobby.lobbies.LobbiesActivity

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                    SignUpScreen(
                        onNavigate = { handleNavigation(it) },
                        viewModel,
                        Modifier.padding(innerPadding))
                }
            }
        }
    }

    private val viewModel : SignUpViewModel by viewModels {
        SignUpViewModel.getFactory(
            (application as DependenciesContainer).authService,
            (application as DependenciesContainer).authInfoRepo
        )
    }

    private fun handleNavigation(it: SignUpNavigation) {
        val intent = when (it) {
            is SignUpNavigation.LobbiesScreen ->
                Intent(this, LobbiesActivity::class.java).apply {
                    putExtra("userInfo", it.userInfo)
                }
        }
        startActivity(intent)
    }

    // For future use
    private suspend fun checkLoggedUser(repo : AuthInfoRepo): Boolean {
        val loggedUser = repo.getAuthInfo()
        return loggedUser != null
    }
}