package com.pdm.pokerdice

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.pdm.pokerdice.ui.lobby.lobbies.CREATE_LOBBY
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesNavigation
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesScreen
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class LobbyCreationTests {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun ClickOn_CreateLobbyButton_GeneratesEvent(){
        var event: LobbiesNavigation? = null

        composeTestRule.setContent {
            LobbiesScreen(onNavigate = {event = it})
        }
        composeTestRule.onNodeWithTag(testTag = CREATE_LOBBY).performClick()
        assert(event == LobbiesNavigation.CreatLobby)
    }
}