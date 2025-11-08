package com.pdm.pokerdice.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.R
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

const val USERNAME_INPUT = "username_input"
const val PASSWORD_INPUT = "password_input"
const val LOGIN_BUTTON = "login_button"
const val ERROR_MESSAGE = "error_message"

sealed class LoginNavigation {
    data class Success(val username: String) : LoginNavigation()
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigate: (LoginNavigation) -> Unit = {},
    onLoginAttempt: (username: String, password: String) -> Boolean = { _, _ -> false }
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo and Title
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                "Poker Dice",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Image(
                painter = painterResource(id = R.drawable.dice),
                contentDescription = "Dice Logo",
                modifier = Modifier.size(80.dp)
            )
        }

        // Login Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = null
                    },
                    label = { Text("Username") },
                    placeholder = { Text("Enter your username") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(USERNAME_INPUT),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = errorMessage != null,
                    enabled = !isLoading
                )

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Hide password"
                                else
                                    "Show password"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PASSWORD_INPUT),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (username.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                val success = onLoginAttempt(username, password)
                                if (success) {
                                    onNavigate(LoginNavigation.Success(username))
                                } else {
                                    errorMessage = "Invalid username or password"
                                    isLoading = false
                                }
                            } else {
                                errorMessage = "Please fill in all fields"
                            }
                        }
                    ),
                    isError = errorMessage != null,
                    enabled = !isLoading
                )

                // Error Message
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.testTag(ERROR_MESSAGE)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Login Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        when {
                            username.isBlank() -> {
                                errorMessage = "Username cannot be empty"
                            }
                            password.isBlank() -> {
                                errorMessage = "Password cannot be empty"
                            }
                            else -> {
                                isLoading = true
                                val success = onLoginAttempt(username, password)
                                if (success) {
                                    onNavigate(LoginNavigation.Success(username))
                                } else {
                                    errorMessage = "Invalid username or password"
                                    isLoading = false
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag(LOGIN_BUTTON),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Logging in..." else "Login",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Info Text
                Text(
                    text = "Anonymous use is not supported",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PokerDiceTheme {
        LoginScreen()
    }
}
