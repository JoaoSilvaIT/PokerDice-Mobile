package com.pdm.pokerdice.login

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.user.isValidCredentialsData

sealed class LoginNavigation {
    object TitleScreen : LoginNavigation()

    object SignUpScreen : LoginNavigation()
}

@Composable
fun LoginScreen(
    onNavigate: (LoginNavigation) -> Unit = {},
    viewModel: LoginScreenViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        val observedState = viewModel.currentState
        LaunchedEffect(observedState) {
            if (observedState is LoginScreenState.LoginSuccess) {
                onNavigate(LoginNavigation.TitleScreen)
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(horizontal = 48.dp)
                .imePadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                LoginForm(
                    loading = observedState is LoginScreenState.LoginInProgress,
                    error = if (observedState is LoginScreenState.LoginError) observedState.errorMessage else "",
                    onLogin = { tentativeCredentials -> viewModel.login(tentativeCredentials) },
                    validateCredentials = ::isValidCredentialsData,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onNavigate(LoginNavigation.SignUpScreen)
                        }
                    )
                }
            }
        }
    }
}
