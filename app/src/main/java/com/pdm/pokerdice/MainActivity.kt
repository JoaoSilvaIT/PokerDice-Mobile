package com.pdm.pokerdice

import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TitleScreen(
                        mod = Modifier.padding(innerPadding),
                        onNavigate = {
                            navigateTo(it)
                        }
                    )
                }
            }
        }
    }

    private fun navigateTo(whereto : TitleScreenActions) {
        val intent = when (whereto) {
            TitleScreenActions.About -> TODO()
            TitleScreenActions.Profile -> Intent()
            TitleScreenActions.StartGame -> TODO()
        }
        startActivity(intent)
    }
}
