package com.pdm.pokerdice
/*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.pdm.pokerdice.domain.lobbies
import com.pdm.pokerdice.ui.lobby.lobbies.CREATE_LOBBY
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesNavigation
import com.pdm.pokerdice.ui.lobby.lobbies.LobbiesScreen
import org.junit.Rule
import org.junit.Test

class LobbiesScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun ClickOn_CreateLobbyButton_GeneratesEvent(){
        var event: LobbiesNavigation? = null

        composeTestRule.setContent {
            LobbiesScreen(onNavigate = {event = it})
        }
        composeTestRule.onNodeWithTag(testTag = CREATE_LOBBY).performClick()
        assert(event == LobbiesNavigation.CreateLobby)
    }

    @Test
    fun ClickOn_JoinLobbyButton_GeneratesEvent(){
        var event: LobbiesNavigation? = null
        val expectedLobby = lobbies[0]

        composeTestRule.setContent {
            LobbiesScreen(onNavigate = {event = it})
        }

        composeTestRule.onNodeWithTag("join_button_${expectedLobby.lid}").assertExists()
        composeTestRule.onNodeWithTag("join_button_${expectedLobby.lid}").performClick()


        assert(event is LobbiesNavigation.SelectLobby)
        assert((event as LobbiesNavigation.SelectLobby).lobby.lid == expectedLobby.lid)
    }


}

 */