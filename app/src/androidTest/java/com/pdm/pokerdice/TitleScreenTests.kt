package com.pdm.pokerdice

// Tests currently commented out
/*
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.pdm.pokerdice.ui.title.ABOUT
import com.pdm.pokerdice.ui.title.PROFILE
import com.pdm.pokerdice.ui.title.START_GAME
import com.pdm.pokerdice.ui.title.TitleScreen
import com.pdm.pokerdice.ui.title.TitleScreenActions
import org.junit.Rule
import org.junit.Test

class TitleScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ClickOn_StartGameButton_GeneratesStartGameEvent(){
        var event: TitleScreenActions? = null

        composeTestRule.setContent {
            TitleScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(START_GAME).performClick()
        assert(event == TitleScreenActions.StartGame)
    }

    @Test
    fun ClickOn_ProfileButton_GeneratesProfileEvent(){
        var event: TitleScreenActions? = null

        composeTestRule.setContent {
            TitleScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(PROFILE).performClick()
        assert(event == TitleScreenActions.Profile)
    }

    @Test
    fun ClickOn_AboutButton_GeneratesAboutEvent(){
        var event: TitleScreenActions? = null

        composeTestRule.setContent {
            TitleScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(ABOUT).performClick()
        assert(event == TitleScreenActions.About)
    }

}

 */
