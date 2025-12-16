package com.pdm.pokerdice.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.NavigationIntent
import kotlin.getValue

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen(
                viewModel = homeScreenViewModel,
                navigator = NavigationIntent(this)
            )
        }
    }

    private val homeScreenViewModel : HomeScreenViewModel by viewModels {
        HomeScreenViewModel.getFactory(
            (application as DependenciesContainer).homeService
        )
    }

    private fun handleNavigation(it : HomeNavigation) {
        when (it) {
            is HomeNavigation.LoginScreen -> {
                // Navigate to Login Screen
            }
            is HomeNavigation.SignUpScreen -> {
                // Navigate to Sign Up Screen
            }
        }
    }

}