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
    fun new_dialog_shows_title_Новая_карта() {
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
    fun edit_dialog_shows_title_Редактировать_карту() {
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
    fun edit_dialog_prefills_with_existing_values() {
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
    fun dialog_always_shows_Save_and_Cancel_buttons() {
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
    fun onDismiss_called_when_Cancel_clicked() {
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
    fun onConfirm_called_with_correct_Card_when_Save_clicked() {
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
    fun new_dialog_prefills_with_empty_values() {
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
    fun edit_dialog_shows_existing_name_and_description_fields() {
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
