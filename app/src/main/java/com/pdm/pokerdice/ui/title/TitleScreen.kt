package com.pdm.pokerdice.ui.title

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.Balatro
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroNavyDark
import com.pdm.pokerdice.ui.theme.RetroTeal

enum class TitleScreenActions {
    About,
    Profile,
    Lobbies,
}

const val LOBBIES = "lobbies_button"
const val PROFILE = "profile_button"
const val ABOUT = "about_button"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleScreen(
    modifier: Modifier,
    onNavigate: (TitleScreenActions) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = "Poker Dice Menu"
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 48.dp) // Increased spacing
            ) {
                Text(
                    "Poker Dice",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 48.sp,
                        fontFamily = Balatro,
                        color = Color.White,
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.dice),
                    contentDescription = "Dice Image",
                    modifier = Modifier.size(64.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp), // Increased spacing between buttons
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)
            ) {
                Button(
                    onClick = { onNavigate(TitleScreenActions.Lobbies) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.testTag(LOBBIES).fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = RetroNavyDark
                    )
                ) {
                    Text("Lobbies Available", style = MaterialTheme.typography.titleSmall, fontFamily = Balatro)
                }
                Button(
                    onClick = { onNavigate(TitleScreenActions.Profile) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.testTag(PROFILE).fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = RetroNavyDark
                    )
                ) {
                    Text("Profile", style = MaterialTheme.typography.titleSmall, fontFamily = Balatro)
                }
                Button(
                    onClick = { onNavigate(TitleScreenActions.About) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.testTag(ABOUT).fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = RetroNavyDark
                    )
                ) {
                    Text("About", style = MaterialTheme.typography.titleSmall, fontFamily = Balatro)
                }
            }
        }
    }
}

@Preview()
@Composable
fun TitleScreenPreview() {
    PokerDiceTheme {
        TitleScreen(Modifier)
    }
}
