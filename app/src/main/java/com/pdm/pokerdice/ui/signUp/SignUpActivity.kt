package com.pdm.pokerdice.ui.signUp

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
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            (application as DependenciesContainer).signUpService,
            (application as DependenciesContainer).authInfoRepo
        )
    }

    private fun handleNavigation(it: SignUpNavigation) {
        val intent = when (it) {
            is SignUpNavigation.LobbiesScreen ->
                Intent(this, LobbiesActivity::class.java).apply {
                    putExtra("token", it.token)
                }

        }
        startActivity(intent)
    }
}