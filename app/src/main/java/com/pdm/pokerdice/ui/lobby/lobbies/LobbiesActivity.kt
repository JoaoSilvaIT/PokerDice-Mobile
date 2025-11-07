    package com.pdm.pokerdice.ui.lobby.lobbies

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.viewModels
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.ui.Modifier
    import com.pdm.pokerdice.DependenciesContainer
    import com.pdm.pokerdice.domain.User
    import com.pdm.pokerdice.repo.mem.RepoLobbyInMem
    import com.pdm.pokerdice.ui.lobby.lobbyCreation.LobbyCreationActivity
    import com.pdm.pokerdice.ui.lobby.lobbyIndividual.LobbyActivity
    import com.pdm.pokerdice.ui.theme.PokerDiceTheme
    import kotlin.getValue

    class LobbiesActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val user = intent.getParcelableExtra("user", User::class.java) ?:
                User(0, "Default User", "")

            setContent {
                PokerDiceTheme {
                    Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                        LobbiesScreen(user,
                            Modifier.padding(innerPadding),
                            onNavigate = { handleNavigation(it) },
                            viewModel)
                    }
                }
            }
        }

        private val viewModel: LobbiesViewModel by viewModels {
            LobbiesViewModel.Companion.getFactory(
                service = (application as DependenciesContainer).lobbyService
            )
        }

        private fun handleNavigation(it: LobbiesNavigation) {
           val intent = when (it) {
               is LobbiesNavigation.CreateLobby ->
                   Intent(this, LobbyCreationActivity::class.java).apply {
                         putExtra("user", it.user)
                   }

               is LobbiesNavigation.SelectLobby ->
                   Intent(this, LobbyActivity::class.java).apply {
                         putExtra("lobby", it.lobby)
                            putExtra("user", it.user)
                   }
           }
            startActivity(intent)
        }
    }