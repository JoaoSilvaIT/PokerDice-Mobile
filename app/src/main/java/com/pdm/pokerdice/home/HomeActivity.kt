package com.pdm.pokerdice.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.login.LoginActivity
import com.pdm.pokerdice.signUp.SignUpActivity
import kotlin.getValue

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen(
                viewModel = homeScreenViewModel,
                onNavigate = { handleNavigation(it) }
            )
        }
    }

    private val homeScreenViewModel : HomeScreenViewModel by viewModels {
        HomeScreenViewModel.getFactory(
            (application as DependenciesContainer).homeService
        )
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