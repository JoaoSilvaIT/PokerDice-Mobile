package com.pdm.pokerdice.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

sealed class AboutNavigation {
    class Creator(val destination: String) : AboutNavigation()
}

@Composable
fun AboutScreen(mod: Modifier, onNavigate: (AboutNavigation) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = mod.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Text("About Poker Dice", fontSize = 40.sp)
        Button(onClick = {onNavigate(AboutNavigation.Creator("https://github.com/pedrowlv"))}){
            Text("Pedro Monteiro")
        }
        Button(onClick = {onNavigate(AboutNavigation.Creator("https://github.com/jacaoo"))}){
            Text("Bernardo Jaco")
        }
        Button(onClick = {onNavigate(AboutNavigation.Creator("https://github.com/JoaoSilvaIT"))}){
            Text("João Silva")
        }
    }
}

@Preview
@Composable
fun aboutPreview() {
    AboutScreen(Modifier)
}

