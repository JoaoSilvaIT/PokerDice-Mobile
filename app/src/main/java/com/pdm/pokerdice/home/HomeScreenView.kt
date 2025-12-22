package com.pdm.pokerdice.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart

const val HOME_VIEW_TAG = "HomeView"
const val HOMEPAGE_LOGIN_BUTTON = "Login button on home page"
const val HOMEPAGE_SIGNUP_BUTTON = "Sign Up button on home page"
const val HOMEPAGE_MADE_BY_TEXT = "made by text on Home page"

@Composable
fun HomeView(
    creators: List<String>,
    onNavigate: (HomeNavigation) -> Unit = {},
) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .testTag(HOME_VIEW_TAG),
        topBar = {
            GenericTopAppBar(
                title = stringResource(R.string.app_name),
                actions = {
                    TextButton(
                        onClick = { onNavigate(HomeNavigation.LoginScreen()) },
                        modifier = Modifier.testTag(HOMEPAGE_LOGIN_BUTTON),
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    TextButton(
                        onClick = { onNavigate(HomeNavigation.SignUpScreen()) },
                        modifier = Modifier.testTag(HOMEPAGE_SIGNUP_BUTTON),
                    ) {
                        Text(
                            text = stringResource(R.string.sign_Up),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PokerDice", // Changed from stringResource(R.string.welcome)
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 64.sp, // Increased further for impact
                            color = Color.White,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(4f, 4f),
                                blurRadius = 8f
                            )
                        ),
                        modifier = Modifier.padding(16.dp),
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Image slot under the title
                    Image(
                        painter = painterResource(id = R.drawable.dice),
                        contentDescription = "Poker Dice Logo",
                        modifier = Modifier.size(200.dp) // Adjust size as needed
                    )
                }
            }
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.testTag(HOMEPAGE_MADE_BY_TEXT),
                    text = stringResource(R.string.creators),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                )
                creators.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
    PokerDiceTheme {
        HomeView(
            creators =
                listOf(
                    "João Silva",
                    "Pedro Monteiro",
                    "Bernardo Jaco",
                ),
            onNavigate = {},
        )
    }
}
