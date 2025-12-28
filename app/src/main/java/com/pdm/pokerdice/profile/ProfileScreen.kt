package com.pdm.pokerdice.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.pdm.pokerdice.R
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroGold
import com.pdm.pokerdice.ui.theme.RetroNavyLight
import com.pdm.pokerdice.ui.theme.RetroOrange
import com.pdm.pokerdice.ui.theme.RetroTeal

sealed class ProfileNavigation {
    data object Back : ProfileNavigation()

    data object Logout : ProfileNavigation()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigate: (ProfileNavigation) -> Unit,
    viewModel: ProfileViewModel,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = "Player Profile",
                onBackAction = { onNavigate(ProfileNavigation.Back) },
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    ),
        ) {
            when (val currentState = state) {
                is ProfileScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = RetroGold)
                }
                is ProfileScreenState.Success -> {
                    ProfileContent(
                        user = currentState.user,
                        inviteCode = currentState.inviteCode,
                        onCreateInvite = viewModel::createInvite,
                        onLogout = {
                            viewModel.logout { onNavigate(ProfileNavigation.Logout) }
                        },
                    )
                }
                is ProfileScreenState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.fetchProfile() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    inviteCode: String?,
    onCreateInvite: () -> Unit,
    onLogout: () -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // User Header
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.dice),
                    contentDescription = "Profile Picture",
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, RetroGold, CircleShape),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        }

        // Account Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = RetroNavyLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Account Balance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${user.balance} Coins",
                        style = MaterialTheme.typography.displaySmall,
                        color = RetroTeal,
                    )
                }
            }
        }

        // Statistics
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = RetroNavyLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Game Statistics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )

                    val stats = user.statistics
                    StatRow("Games Played", stats.gamesPlayed.toString())
                    StatRow("Wins", stats.wins.toString())
                    StatRow("Losses", stats.losses.toString())
                    StatRow("Win Rate", "${String.format("%.1f", stats.winRate)}%")
                }
            }
        }

        // Hand Frequencies
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = RetroNavyLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Hand Frequencies",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )

                    val frequencies = user.statistics.handFrequencies
                    if (frequencies.isEmpty()) {
                        Text(
                            text = "No hands played yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    } else {
                        frequencies.entries.sortedByDescending { it.key.strength }.forEach { (rank, count) ->
                            StatRow(
                                label = rank.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                                value = count.toString()
                            )
                        }
                    }
                }
            }
        }

        // Invite Friend
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = RetroNavyLight)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "Invite a Friend",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )

                    if (inviteCode != null) {
                        val clipboardManager = LocalClipboardManager.current
                        val context = LocalContext.current

                        Text(
                            text = "Share this code:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = inviteCode,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RetroTeal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = {
                                clipboardManager.setText(AnnotatedString(inviteCode))
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Copy", color = RetroGold)
                            }
                        }
                    } else {
                        Button(
                            onClick = onCreateInvite,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = RetroGold, contentColor = RetroNavyLight)
                        ) {
                            Text("Generate Invite Code")
                        }
                    }
                }
            }
        }

        // Logout
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = RetroOrange,
                        contentColor = Color.White,
                    ),
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = RetroGold)
    }
}
