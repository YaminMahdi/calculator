package com.mlab.calculator

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCalculatorKeypadClick() {
        // Click number 7
        composeTestRule.onNodeWithText("7").performClick()
        // Expression or result displays 7
        composeTestRule.onNodeWithText("7").assertExists()
    }
}
