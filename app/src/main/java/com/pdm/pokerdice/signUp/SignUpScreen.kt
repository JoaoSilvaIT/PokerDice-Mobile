package com.pdm.pokerdice.signUp

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
import com.pdm.pokerdice.domain.user.isValidNewCredentialsData


sealed class SignUpNavigation {
    class LobbiesScreen(val token : String) : SignUpNavigation()
}

@Composable
fun SignUpScreen(
    onNavigate: (SignUpNavigation) -> Unit = {},
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        val observedState = viewModel.currentState
        LaunchedEffect(observedState) {
            if (observedState is SignUpState.SignUpSuccess) {
                val token = observedState.token
                onNavigate(SignUpNavigation.LobbiesScreen(token))
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
            SignUpForm(
                loading = observedState is SignUpState.SignUpInProgress,
                error = if (observedState is SignUpState.SignUpError) observedState.errorMessage else "",
                onSignUp = { tentativeCredentials -> viewModel.signUp(tentativeCredentials) },
                validateCredentials = ::isValidNewCredentialsData,
                modifier = modifier
            )
        }
    }
}