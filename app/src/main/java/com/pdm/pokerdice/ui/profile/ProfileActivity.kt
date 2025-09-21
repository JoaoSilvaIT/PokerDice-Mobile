package com.pdm.pokerdice.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            PokerDiceTheme{
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) {
                        innerPadding ->
                    ProfileScreen(
                        Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }
}