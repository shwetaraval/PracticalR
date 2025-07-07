package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import coil.ImageLoader
import com.example.myapplication.ui.schedule.ScheduleUI
import org.junit.Rule
import org.junit.Test
import java.time.YearMonth

class ScheduleUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun scheduleUI_rendersStickyHeadersAndGamesCorrectly() {
        val testData = listOf(
            YearMonth.of(2024, 1) to listOf(
                MockGameFactory.create("MIA", "WAS", isFuture = false),
                MockGameFactory.create("NOP", "MIA", isFuture = true)
            ),
            YearMonth.of(2024, 2) to listOf(
                MockGameFactory.create("PHI", "MIA", isFuture = false)
            )
        )

        composeRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val testImageLoader = ImageLoader.Builder(context).build()

            ScheduleUI(
                uiState = testData,
                listState = androidx.compose.foundation.lazy.rememberLazyListState(),
                onMonthNavigate = {},
                imageLoader = testImageLoader
            )
        }

        // Check headers exist
        composeRule.onNodeWithText("January 2024").assertIsDisplayed()
        composeRule.onNodeWithText("February 2024").assertIsDisplayed()

        // Check team names
        composeRule.onNodeWithText("MIA").assertIsDisplayed()
        composeRule.onNodeWithText("WAS").assertIsDisplayed()

        // Check ticket UI (for future game)
        composeRule.onNodeWithText("Buy ticket on").assertIsDisplayed()
    }
}
