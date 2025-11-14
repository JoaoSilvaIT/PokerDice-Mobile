package com.pdm.pokerdice.ui.authentication.login

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
import com.pdm.pokerdice.login_signup.isValidCredentialsData
import com.pdm.pokerdice.login_signup.isValidNewCredentialsData
import com.pdm.pokerdice.ui.authentication.AuthenticationNavigation

sealed class LoginNavigation {
    class LobbiesScreen(val token : String) : LoginNavigation()
}

@Composable
fun LoginScreen(
    onNavigate: (AuthenticationNavigation) -> Unit = {},
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        val observedState = viewModel.currentState
        LaunchedEffect(observedState) {
            if (observedState is LoginState.LoginSuccess) {
                val token = observedState.token
                onNavigate(AuthenticationNavigation.LobbiesScreen(token))
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
                loading = observedState is LoginState.LoginInProgress,
                error = if (observedState is LoginState.LoginError) observedState.errorMessage else "",
                onLogin = { tentativeCredentials -> viewModel.login(tentativeCredentials) },
                validateCredentials = ::isValidCredentialsData,
                modifier = modifier
            )
        }
    }
}