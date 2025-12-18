package com.pdm.pokerdice.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.user.isValidCredentialsData

sealed class LoginNavigation {
    object TitleScreen : LoginNavigation()
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
            LoginForm(
                loading = observedState is LoginScreenState.LoginInProgress,
                error = if (observedState is LoginScreenState.LoginError) observedState.errorMessage else "",
                onLogin = { tentativeCredentials -> viewModel.login(tentativeCredentials) },
                validateCredentials = ::isValidCredentialsData,
                modifier = modifier
            )
        }
    }
}
