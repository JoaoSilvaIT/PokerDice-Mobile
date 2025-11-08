package com.pdm.pokerdice.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.repo.RepositoryUser
import com.pdm.pokerdice.repo.mem.RepoUserInMem
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.MainActivity

class LoginActivity : ComponentActivity() {

    private val userRepository: RepositoryUser = RepoUserInMem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokerDiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        modifier = Modifier,
                        onNavigate = { navigation ->
                            when (navigation) {
                                is LoginNavigation.Success -> {
                                    // Navigate to Title Screen on successful login
                                    val intent = Intent(this, MainActivity::class.java).apply {
                                        putExtra("username", navigation.username)
                                        // Clear the back stack so user can't go back to login
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        },
                        onLoginAttempt = { username, password ->
                            // Fake authentication logic
                            // In Milestone 5, this will be replaced with HTTP API call
                            authenticateUser(username, password)
                        }
                    )
                }
            }
        }
    }

    /**
     * Fake authentication method for Milestone 3
     * This will be replaced with actual HTTP API call in Milestone 5
     *
     * For now, accepts any non-empty credentials that exist in RepoUserInMem
     * Default test users: "admin"/"admin123", "user1"/"password1", "user2"/"password2"
     */
    private fun authenticateUser(username: String, password: String): Boolean {
        // Simple fake validation
        if (username.isBlank() || password.isBlank()) {
            return false
        }

        // Check if user exists in fake repository
        val user = (userRepository as RepoUserInMem).findByName(username)

        // For Milestone 3: accept if user exists or create new user
        return if (user != null) {
            // In real implementation, would check password hash
            // For now, just check if password matches the stored one
            user.password == password
        } else {
            // For development: auto-create user with given credentials
            userRepository.createUserWithPassword(username, password)
            true
        }
    }
}

