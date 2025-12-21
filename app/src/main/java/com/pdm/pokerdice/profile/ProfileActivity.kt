package com.pdm.pokerdice.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.home.HomeActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class ProfileActivity : ComponentActivity() {
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.getFactory((application as DependenciesContainer).authService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerDiceTheme {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigate = { handleNavigation(it) },
                )
            }
        }
    }

    private fun handleNavigation(action: ProfileNavigation) {
        when (action) {
            ProfileNavigation.Back -> finish()
            ProfileNavigation.Logout -> {
                val intent =
                    Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }
        }
    }
}
