package com.pdm.pokerdice.ui.title

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

enum class TitleScreenActions {
    About,
    Profile,
    StartGame,
}

const val START_GAME = "start_game_button"
const val PROFILE = "profile_button"
const val ABOUT = "about_button"

@Composable
fun TitleScreen(
    modifier: Modifier,
    onNavigate: (TitleScreenActions) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Poker Dice", fontSize = 40.sp)
            Image(
                painter = painterResource(id = R.drawable.dice),
                contentDescription = "Dice Image",
                modifier = Modifier.size(90.dp),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { onNavigate(TitleScreenActions.StartGame) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.testTag(START_GAME)
            ) {
                Text("Start Game")
            }
            Button(
                onClick = { onNavigate(TitleScreenActions.Profile) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.testTag(PROFILE)
            ) {
                Text("Profile")
            }
            Button(
                onClick = { onNavigate(TitleScreenActions.About) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.testTag(ABOUT)
            ) {
                Text("About")
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
