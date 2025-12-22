package com.pdm.pokerdice.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.domain.user.isValidCredentialsData
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroGold

sealed class LoginNavigation {
    object TitleScreen : LoginNavigation()

    object SignUpScreen : LoginNavigation()
}

@Composable
fun LoginScreen(
    onNavigate: (LoginNavigation) -> Unit = {},
    viewModel: LoginScreenViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = stringResource(R.string.login),
                onBackAction = { /* Optional: Navigate back or finish activity */ }
            )
        }
    ) { innerPadding ->
        val observedState = viewModel.currentState
        LaunchedEffect(observedState) {
            if (observedState is LoginScreenState.LoginSuccess) {
                onNavigate(LoginNavigation.TitleScreen)
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(paddingValues = innerPadding)
                    .padding(horizontal = 48.dp)
                    .imePadding(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier,
            ) {
                LoginForm(
                    loading = observedState is LoginScreenState.LoginInProgress,
                    error = if (observedState is LoginScreenState.LoginError) observedState.errorMessage else "",
                    onLogin = { tentativeCredentials -> viewModel.login(tentativeCredentials) },
                    validateCredentials = ::isValidCredentialsData,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RetroGold,
                        modifier =
                            Modifier.clickable {
                                onNavigate(LoginNavigation.SignUpScreen)
                            },
                    )
                }
            }
        }
    }
}
