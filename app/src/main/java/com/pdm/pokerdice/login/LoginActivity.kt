package com.pdm.pokerdice.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.ui.title.TitleActivity

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        onNavigate = { handleNavigation(it) },
                        viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private val viewModel: LoginScreenViewModel by viewModels {
        LoginScreenViewModel.getFactory(
            (application as DependenciesContainer).authService,
            (application as DependenciesContainer).authInfoRepo
        )
    }

    private fun handleNavigation(it: LoginNavigation) {
         val intent = when (it) {
             is LoginNavigation.TitleScreen ->
                 Intent(this, TitleActivity::class.java)
         }
        startActivity(intent)
    }
}
