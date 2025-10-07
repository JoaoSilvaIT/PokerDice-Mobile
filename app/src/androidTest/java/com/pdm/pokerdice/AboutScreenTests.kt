package com.pdm.pokerdice

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import AboutNavigation
import AboutScreen
import CONTACT_US
import CREATOR1
import CREATOR2
import CREATOR3
import GAME_RULES
import org.junit.Rule
import org.junit.Test

class AboutScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ClickOn_GameRulesButton_GeneratesGameRulesEvent(){
        var event: AboutNavigation? = null

        composeTestRule.setContent {
            AboutScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(GAME_RULES).performClick()
        assert(event == AboutNavigation.GameRules)
    }

    @Test
    fun ClickOn_ContactUsButton_GeneratesContactUsEvent(){
        var event: AboutNavigation? = null

        composeTestRule.setContent {
            AboutScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(CONTACT_US).performClick()
        assert(event == AboutNavigation.ContactUs)
    }

    @Test
    fun ClickOn_Creator1Button_GeneratesCreatorEvent(){
        var event: AboutNavigation? = null
        val expectedUrl = "https://github.com/pedrowlv"

        composeTestRule.setContent {
            AboutScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(CREATOR1).performClick()
        assert(event is AboutNavigation.Creator)
        assert((event as AboutNavigation.Creator).destination == expectedUrl)
    }

    @Test
    fun ClickOn_Creator2Button_GeneratesCreatorEvent(){
        var event: AboutNavigation? = null
        val expectedUrl = "https://github.com/JoaoSilvaIT"

        composeTestRule.setContent {
            AboutScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(CREATOR2).performClick()
        assert(event is AboutNavigation.Creator)
        assert((event as AboutNavigation.Creator).destination == expectedUrl)
    }

    @Test
    fun ClickOn_Creator3Button_GeneratesCreatorEvent(){
        var event: AboutNavigation? = null
        val expectedUrl = "https://github.com/jacaoo"

        composeTestRule.setContent {
            AboutScreen(modifier = Modifier, onNavigate = { event = it })
        }

        composeTestRule.onNodeWithTag(CREATOR3).performClick()
        assert(event is AboutNavigation.Creator)
        assert((event as AboutNavigation.Creator).destination == expectedUrl)
    }
}
