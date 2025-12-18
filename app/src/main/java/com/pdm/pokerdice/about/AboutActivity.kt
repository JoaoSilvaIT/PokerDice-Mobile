package com.pdm.pokerdice.about

import AboutNavigation
import AboutScreen
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AboutScreen(
                        Modifier.padding(innerPadding), onNavigate = { handleNavigation(it) }
                    )
                }
            }
        }
    }

    private fun handleNavigation(it: AboutNavigation) {
        when (it) {
            is AboutNavigation.Creator -> navigateToURL(it.destination)
            AboutNavigation.GameRules -> navigateToURL("https://en.wikipedia.org/wiki/Poker_dice")
            AboutNavigation.ContactUs -> sendGroupEmail()
        }
    }

    private fun navigateToURL(destination: String) {
        val webpage: Uri = destination.toUri()
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    private fun sendGroupEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(
                Intent.EXTRA_EMAIL, arrayOf(
                    "a51457@alunos.isel.pt",
                    "a51682@alunos.isel.pt",
                    "a51690@alunos.isel.pt"
                )
            )
            putExtra(Intent.EXTRA_SUBJECT, "Poker Dice Feedback")
        }
        startActivity(intent)
    }
}
