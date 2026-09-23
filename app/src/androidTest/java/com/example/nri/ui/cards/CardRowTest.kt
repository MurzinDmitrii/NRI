package com.example.nri.ui.cards

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.nri.data.Card
import org.junit.Rule
import org.junit.Test

class CardRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `cardRow displays card name`() {
        val card = Card(id = 1, name = "Карта Огня", description = "Огненный шар")

        composeTestRule.setContent {
            CardRow(
                card = card,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(card.name).assertIsDisplayed()
    }

    @Test
    fun `cardRow displays description when not blank`() {
        val card = Card(id = 1, name = "Карта Льда", description = "Ледяная стрела")

        composeTestRule.setContent {
            CardRow(
                card = card,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(card.description).assertIsDisplayed()
    }

    @Test
    fun `cardRow does not display description when blank`() {
        val card = Card(id = 1, name = "Простая карта", description = "")

        composeTestRule.setContent {
            CardRow(
                card = card,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(card.name).assertIsDisplayed()
    }

    @Test
    fun `cardRow does not display description when whitespace`() {
        val card = Card(id = 1, name = "Карта", description = "   ")

        composeTestRule.setContent {
            CardRow(
                card = card,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(card.name).assertIsDisplayed()
    }
}
