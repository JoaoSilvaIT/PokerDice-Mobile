package com.pdm.pokerdice.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreen(
                        mod = Modifier.padding(innerPadding),
                        onNavigate = { handleProfileNavigation(it) },
                    )
                }
            }
        }
    }

    private fun handleProfileNavigation(navigation: ProfileNavigation) {
        when (navigation) {
            is ProfileNavigation.EditProfile -> {
                // Navigate to edit profile screen or show dialog
                Toast.makeText(this, "Edit Profile functionality coming soon", Toast.LENGTH_SHORT).show()
            }
            is ProfileNavigation.GameHistory -> {
                // Navigate to game history screen
                Toast.makeText(this, "Game History functionality coming soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
