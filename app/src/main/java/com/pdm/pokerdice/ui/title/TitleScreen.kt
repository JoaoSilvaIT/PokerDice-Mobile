package com.pdm.pokerdice.ui.title

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

enum class TitleScreenActions {
    About,
    Profile,
    Lobbies,
}
const val LOBBIES = "lobbies_button"
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
            Text("Poker Dice", style = MaterialTheme.typography.headlineLarge)
            Image(
                painter = painterResource(id = R.drawable.dice),
                contentDescription = "Dice Image",
                modifier = Modifier.size(90.dp),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { onNavigate(TitleScreenActions.Lobbies) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.testTag(LOBBIES)
            ) {
                Text("Lobbies Available", style = MaterialTheme.typography.titleSmall)
            }
            Button(
                onClick = { onNavigate(TitleScreenActions.Profile) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.testTag(PROFILE)
            ) {
                Text("Profile", style = MaterialTheme.typography.titleSmall)
            }
            Button(
                onClick = { onNavigate(TitleScreenActions.About) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.testTag(ABOUT)
            ) {
                Text("About", style = MaterialTheme.typography.titleSmall)
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
