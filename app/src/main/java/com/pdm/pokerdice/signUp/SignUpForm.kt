package com.pdm.pokerdice.signUp

import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.domain.user.NewUserCredentials
import com.pdm.pokerdice.ui.theme.RetroGold
import com.pdm.pokerdice.ui.theme.RetroNavyDark
import com.pdm.pokerdice.ui.theme.RetroTeal
import com.pdm.pokerdice.ui.theme.RetroTextDark

@Composable
fun SignUpForm(
    loading: Boolean,
    error: String?,
    onSignUp: (credentials: NewUserCredentials) -> Unit,
    validateCredentials: (name: String, email: String, password: String, invite: String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val userName = rememberSaveable { mutableStateOf("") }
    val inviteCode = rememberSaveable { mutableStateOf("") }

    SignUpFormStateLess(
        loading,
        error,
        userName.value,
        email.value,
        password.value,
        inviteCode.value,
        isDataValid = validateCredentials(userName.value, email.value, password.value, inviteCode.value),
        onEmailChange = { email.value = it },
        onPasswordChange = { password.value = it },
        onUserNameChange = { userName.value = it },
        onInviteCodeChange = { inviteCode.value = it },
        onSignUp = { name, em, pass, invite -> onSignUp(NewUserCredentials(name, em, pass, invite)) },
        modifier = modifier,
    )
}

@Composable
fun SignUpFormStateLess(
    loading: Boolean,
    error: String?,
    userName: String,
    email: String,
    password: String,
    inviteCode: String,
    isDataValid: Boolean,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onInviteCodeChange: (String) -> Unit,
    onSignUp: (name: String, email: String, password: String, invite: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
            .padding(24.dp), // Added container card style
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RetroGold,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = RetroNavyDark,
            cursorColor = RetroTeal,
            focusedTextColor = RetroTextDark,
            unfocusedTextColor = RetroTextDark,
        )

        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            label = { Text("Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.small
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.small
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.small
        )

        OutlinedTextField(
            value = inviteCode,
            onValueChange = onInviteCodeChange,
            label = { Text("Invite Code") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (isDataValid && !loading) onSignUp(userName, email, password, inviteCode) }),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = MaterialTheme.shapes.small
        )

        if (!error.isNullOrBlank()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { onSignUp(userName, email, password, inviteCode) },
            enabled = isDataValid && !loading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = RetroTeal,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            if (loading) {
                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
                Spacer(Modifier.width(8.dp))
            }
            Text("Sign Up")
        }
    }
}
