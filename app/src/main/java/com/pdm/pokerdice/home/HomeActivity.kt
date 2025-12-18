package com.pdm.pokerdice.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pdm.pokerdice.login.LoginActivity
import com.pdm.pokerdice.signUp.SignUpActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokerDiceTheme {
                HomeScreen(
                    onNavigate = { handleNavigation(it) }
                )
            }
        }
    }

    private fun handleNavigation(it : HomeNavigation) {
        val intent = when (it) {
            is HomeNavigation.LoginScreen -> {
                Intent(this, LoginActivity::class.java)
            }
            is HomeNavigation.SignUpScreen -> {
                Intent(this, SignUpActivity::class.java)
            }
        }
        startActivity(intent)
    }
}
