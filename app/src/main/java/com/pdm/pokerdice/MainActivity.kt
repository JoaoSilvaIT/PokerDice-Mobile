package com.pdm.pokerdice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.about.AboutActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokerDiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TitleScreen(
                        modifier= Modifier.padding(innerPadding),
                        onNavigate = { navigateTo(it)}
                    )
                }
            }
        }
    }

    private fun navigateTo(action: TitleScreenActions) {
        val intent = when(action){
            TitleScreenActions.About -> Intent(this, AboutActivity::class.java)
            TitleScreenActions.Profile -> TODO()
            TitleScreenActions.StartGame -> TODO()
        }
        startActivity(intent)
    }

}
