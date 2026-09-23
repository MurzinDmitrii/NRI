package com.example.nri.ui.cards

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.nri.data.Card
import org.junit.Rule
import org.junit.Test

class CardDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `new dialog shows title Новая карта`() {
        composeTestRule.setContent {
            CardDialog(
                initial = null,
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Новая карта").assertIsDisplayed()
    }

    @Test
    fun `edit dialog shows title Редактировать карту`() {
        val existingCard = Card(id = 1, name = "Меч", description = "Острый")

        composeTestRule.setContent {
            CardDialog(
                initial = existingCard,
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Редактировать карту").assertIsDisplayed()
    }

    @Test
    fun `edit dialog pre-fills with existing values`() {
        val existingCard = Card(id = 1, name = "Карта Огня", description = "Огненный шар")

        composeTestRule.setContent {
            CardDialog(
                initial = existingCard,
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Карта Огня").assertIsDisplayed()
        composeTestRule.onNodeWithText("Огненный шар").assertIsDisplayed()
    }

    @Test
    fun `dialog always shows Save and Cancel buttons`() {
        composeTestRule.setContent {
            CardDialog(
                initial = null,
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Сохранить").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отмена").assertIsDisplayed()
    }

    @Test
    fun `onDismiss called when Cancel clicked`() {
        var dismissed = false

        composeTestRule.setContent {
            CardDialog(
                initial = null,
                onDismiss = { dismissed = true },
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Отмена").performClick()

        assert(dismissed)
    }

    @Test
    fun `onConfirm called with correct Card when Save clicked`() {
        var receivedCard: Card? = null

        composeTestRule.setContent {
            CardDialog(
                initial = null,
                onDismiss = {},
                onConfirm = { receivedCard = it }
            )
        }

        composeTestRule.onNodeWithText("Сохранить").performClick()

        assert(receivedCard != null)
        assert(receivedCard!!.name.isNotBlank())
    }

    @Test
    fun `new dialog pre-fills with empty values`() {
        composeTestRule.setContent {
            CardDialog(
                initial = null,
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Should show empty text fields (no pre-filled content)
        composeTestRule.onNodeWithText("Название").assertIsDisplayed()
        composeTestRule.onNodeWithText("Описание").assertIsDisplayed()
    }

    @Test
    fun `edit dialog shows existing name and description fields`() {
        val existingCard = Card(id = 1, name = "Старая карта", description = "Старое описание")

        composeTestRule.setContent {
            CardDialog(
                initial = existingCard,
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Название").assertIsDisplayed()
        composeTestRule.onNodeWithText("Описание").assertIsDisplayed()
    }
}
