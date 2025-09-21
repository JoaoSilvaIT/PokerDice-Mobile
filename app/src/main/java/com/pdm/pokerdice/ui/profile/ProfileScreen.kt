package com.pdm.pokerdice.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(mod: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = mod.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Your Profile", fontSize = 40.sp)
    }
}