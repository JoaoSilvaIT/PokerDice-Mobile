package com.pdm.pokerdice

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.pdm.pokerdice.ui.lobby.lobbyCreation.CREATE_LOBBY
import com.pdm.pokerdice.ui.lobby.lobbyCreation.DESCRIPTION
import com.pdm.pokerdice.ui.lobby.lobbyCreation.INCREMENT_LIMIT
import com.pdm.pokerdice.ui.lobby.lobbyCreation.LOBBY_NAME
import com.pdm.pokerdice.ui.lobby.lobbyCreation.LobbyCreationNavigation
import com.pdm.pokerdice.ui.lobby.lobbyCreation.LobbyCreationScreen
import com.pdm.pokerdice.ui.lobby.lobbyCreation.PLAYER_MIN
import org.junit.Rule
import org.junit.Test

class LobbyCreationScreenTests {


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ClickOn_CreateLobbyButton_GeneratesCreatedLobbyEvent() {
        var event: LobbyCreationNavigation? = null
        val lobbyName = "Test Lobby"
        val lobbyDescription = "Test Description"

        composeTestRule.setContent {
            LobbyCreationScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(LOBBY_NAME).performTextInput(lobbyName)
        composeTestRule.onNodeWithTag(DESCRIPTION).performTextInput(lobbyDescription)
        composeTestRule.onNodeWithTag(INCREMENT_LIMIT).performClick()
        composeTestRule.onNodeWithTag(CREATE_LOBBY).performClick()

        assert(event is LobbyCreationNavigation.CreatedLobby)
        val createdLobby = (event as LobbyCreationNavigation.CreatedLobby).lobby
        assert(createdLobby.name == lobbyName)
        assert(createdLobby.description == lobbyDescription)
        assert(createdLobby.limit == 3)
    }
}