package com.pdm.pokerdice.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.user.UserCredentials

@Composable
fun LoginForm(
    loading: Boolean,
    error: String?,
    onLogin: (credentials: UserCredentials) -> Unit,
    validateCredentials: (email: String, password: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LoginFormStateLess(
        loading,
        error,
        email,
        password,
        isDataValid = validateCredentials(email, password),
        onEmailChange = {email = it},
        onPasswordChange = {password = it},
        onLogin = {email, password -> onLogin(UserCredentials(email, password))},
        modifier = modifier
    )
}

@Composable
fun LoginFormStateLess(
    loading: Boolean,
    error: String?,
    email: String,
    password: String,
    isDataValid: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (isDataValid && !loading) onLogin( email, password) }),
            modifier = Modifier.fillMaxWidth()
        )

        if (!error.isNullOrBlank()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { onLogin(email, password) },
            enabled = isDataValid && !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text("Entrar")
        }
    }
}