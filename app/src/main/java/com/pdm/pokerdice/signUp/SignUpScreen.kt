package com.pdm.pokerdice.signUp

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
import com.pdm.pokerdice.domain.user.isValidNewCredentialsData
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroGold

sealed class SignUpNavigation {
    object TitleScreen : SignUpNavigation()

    object LoginScreen : SignUpNavigation()
}

@Composable
fun SignUpScreen(
    onNavigate: (SignUpNavigation) -> Unit = {},
    viewModel: SignUpScreenViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = stringResource(R.string.sign_Up),
                onBackAction = { /* Optional: Navigate back */ }
            )
        }
    ) { innerPadding ->
        val observedState = viewModel.currentState
        LaunchedEffect(observedState) {
            if (observedState is SignUpScreenState.SignUpSuccess) {
                onNavigate(SignUpNavigation.TitleScreen)
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
                SignUpForm(
                    loading = observedState is SignUpScreenState.SignUpInProgress,
                    error = if (observedState is SignUpScreenState.SignUpError) observedState.errorMessage else "",
                    onSignUp = { tentativeCredentials -> viewModel.signUp(tentativeCredentials) },
                    validateCredentials = ::isValidNewCredentialsData,
                    modifier = modifier,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RetroGold,
                        modifier =
                            Modifier.clickable {
                                onNavigate(SignUpNavigation.LoginScreen)
                            },
                    )
                }
            }
        }
    }
}
