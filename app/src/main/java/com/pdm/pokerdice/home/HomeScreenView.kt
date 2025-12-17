package com.pdm.pokerdice.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chimp.ui.common.GenericTopAppBar
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.PokerDiceTheme


const val HOME_VIEW_TAG = "HomeView"
const val HOMEPAGE_LOGIN_BUTTON = "Login button on home page"
const val HOMEPAGE_SIGNUP_BUTTON = "Sign Up button on home page"
const val HOMEPAGE_MADE_BY_TEXT = "made by text on Home page"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    creators: List<String>,
    onNavigate : (HomeNavigation) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(HOME_VIEW_TAG),
        topBar = {
            GenericTopAppBar(
                title = stringResource(R.string.app_name),
                actions = {
                    TextButton(
                        onClick = { onNavigate(HomeNavigation.LoginScreen()) },
                        modifier = Modifier.testTag(HOMEPAGE_LOGIN_BUTTON)
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    TextButton(
                        onClick = { onNavigate(HomeNavigation.SignUpScreen()) },
                        modifier = Modifier.testTag(HOMEPAGE_SIGNUP_BUTTON)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_Up),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.testTag(HOMEPAGE_MADE_BY_TEXT),
                    text = stringResource(R.string.creators),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                creators.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
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
            creators = listOf(
                "João Silva",
                "Pedro Monteiro",
                "Bernardo Jaco",
            ),
            onNavigate = {}
        )
    }
}