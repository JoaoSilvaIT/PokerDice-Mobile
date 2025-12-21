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
import com.pdm.pokerdice.login.LoginActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.TitleActivity

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

    private val viewModel : SignUpScreenViewModel by viewModels {
        SignUpScreenViewModel.getFactory(
            (application as DependenciesContainer).authService,
            (application as DependenciesContainer).authInfoRepo
        )
    }

    private fun handleNavigation(it: SignUpNavigation) {
        val intent = when (it) {
            is SignUpNavigation.TitleScreen -> Intent(this, TitleActivity::class.java)
            is SignUpNavigation.LoginScreen -> Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
    }
}