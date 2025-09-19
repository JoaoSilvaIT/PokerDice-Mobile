package com.pdm.pokerdice.about

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

class AboutActivity: ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            PokerDiceTheme{
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) {
                    innerPadding ->
                    AboutScreen(
                        Modifier.padding(paddingValues = innerPadding), onNavigate = {handleNavigation(it)}
                    )
                }
            }
        }
    }

    private fun handleNavigation(it: AboutNavigation) {
        when (it) {
            is AboutNavigation.Creator -> navigateToURL(it.destination)
        }
    }

    private fun navigateToURL(destination: String) {
        val webpage: Uri = destination.toUri()
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

}