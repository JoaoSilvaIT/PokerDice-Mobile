package com.pdm.pokerdice.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.pokerdice.DependenciesContainer
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.game.GameActivity
import com.pdm.pokerdice.ui.theme.PokerDiceTheme
import com.pdm.pokerdice.ui.title.TitleActivity

class LobbyActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context, lobby: Lobby? = null, user: UserExternalInfo? = null) {
            val intent = Intent(context, LobbyActivity::class.java).apply {
                if (lobby != null) putExtra("lobby", lobby)
                if (user != null) putExtra("user", user)
            }
            context.startActivity(intent)
        }
    }

    private val viewModel: LobbyViewModel by viewModels {
        LobbyViewModel.getFactory(
            service = (application as DependenciesContainer).lobbyService,
            gameService = (application as DependenciesContainer).gameService,
            repo = (application as DependenciesContainer).authInfoRepo
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lobby = intent.getParcelableExtra("lobby", Lobby::class.java)
        val user = intent.getParcelableExtra("user", UserExternalInfo::class.java)
        
        // Initialize VM with intent data
        viewModel.initialize(lobby, user)

        setContent {
            PokerDiceTheme {
                LobbyScreen(
                    viewModel = viewModel,
                    onNavigate = { handleNavigation(it) }
                )
            }
        }
    }

    private fun handleNavigation(action: LobbyNavigation) {
        when (action) {
            is LobbyNavigation.StartGame -> GameActivity.navigate(this, action.gameId)
            LobbyNavigation.Back -> finish() // Or navigate to Lobbies/Title
        }
    }
}
