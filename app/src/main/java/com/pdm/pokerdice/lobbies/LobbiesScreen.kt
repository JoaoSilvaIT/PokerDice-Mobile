package com.pdm.pokerdice.lobbies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo

sealed class LobbiesNavigation {
    class SelectLobby(val lobby: Lobby, val user: UserExternalInfo) : LobbiesNavigation()
    object CreateLobby : LobbiesNavigation()
}
@Composable
fun LobbiesScreen(
    modifier: Modifier,
    onNavigate: (LobbiesNavigation) -> Unit = {},
    viewModel: LobbiesViewModel
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is LobbiesScreenState.Loading -> {
            LobbiesScreenView(
                lobbies = null,
            )
        }
        is LobbiesScreenState.ViewLobbies -> {
            val lobbies = (state as LobbiesScreenState.ViewLobbies).lobbies
            LobbiesScreenView(
                lobbies = lobbies,
                onJoinLobby = { lobbyId ->
                    viewModel.joinLobby(lobbyId)
                },
                onCreateLobby = { onNavigate(LobbiesNavigation.CreateLobby) }
            )
        }
        is LobbiesScreenState.JoinLobby -> {
            val lobby = (state as LobbiesScreenState.JoinLobby).lobby
            val user = (state as LobbiesScreenState.JoinLobby).user
            onNavigate(LobbiesNavigation.SelectLobby(lobby, user))
        }
        is LobbiesScreenState.Error -> {
            val errorState = (state as LobbiesScreenState.Error).message
            LobbiesScreenView(
                lobbies = null,
                error = errorState
            )
        }
    }
    /*
    LaunchedEffect(currentJoinState) {
        if (currentJoinState is JoinLobbyState.Success) {
            val lobbyToJoin = (currentJoinState as JoinLobbyState.Success).lobby
            val user = (currentJoinState as JoinLobbyState.Success).user
            onNavigate(LobbiesNavigation.SelectLobby(lobbyToJoin,user))
        }
    }

     */
    /*
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Available Lobbies",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(lobbies) { lobby ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lobby.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = {
                                viewModel.joinLobby(lobby.lid)
                            },
                            modifier = Modifier.testTag("join_button_${lobby.lid}")

                        ) {
                            Text(
                                text = "Join Lobby"
                            )

                        }
                    }
                }
            }

        }
        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    onNavigate(LobbiesNavigation.CreateLobby)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp)
                .testTag(CREATE_LOBBY),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Create Lobby")
        }
    }

     */
}

/*
@Preview()
@Composable
fun LobbiesScreenPreview() {
    val repositoryLobby = RepoLobbyInMem()
    PokerDiceTheme {
        LobbiesScreen(User(1, "NULL", "null"),repositoryLobby, Modifier)
    }
}

 */