package com.example.nri.ui.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NoteDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dialog_shows_custom_title() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Новая заметка").assertIsDisplayed()
    }

    @Test
    fun dialog_shows_label_Заголовок() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Редактировать",
                initialTitle = "Тест",
                initialContent = "",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Заголовок").assertIsDisplayed()
    }

    @Test
    fun dialog_shows_label_Содержание() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Содержание").assertIsDisplayed()
    }

    @Test
    fun dialog_prefills_title_and_content() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Редактировать",
                initialTitle = "Старый заголовок",
                initialContent = "Старое содержание",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Старый заголовок").assertIsDisplayed()
        composeTestRule.onNodeWithText("Старое содержание").assertIsDisplayed()
    }

    @Test
    fun dialog_always_shows_Save_and_Cancel_buttons() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Сохранить").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отмена").assertIsDisplayed()
    }

    @Test
    fun onDismiss_called_when_Cancel_clicked() {
        var dismissed = false

        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = { dismissed = true },
                onConfirm = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Отмена").performClick()

        assert(dismissed)
    }

    @Test
    fun onConfirm_called_with_title_and_content_when_Save_clicked() {
        var receivedTitle: String? = null
        var receivedContent: String? = null

        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = {},
                onConfirm = { title, content ->
                    receivedTitle = title
                    receivedContent = content
                }
            )
        }

        composeTestRule.onNodeWithText("Сохранить").performClick()

        assert(receivedTitle != null)
        assert(receivedContent != null)
    }

    @Test
    fun new_dialog_prefills_with_empty_values() {
        composeTestRule.setContent {
            NoteDialog(
                title = "Новая заметка",
                initialTitle = "",
                initialContent = "",
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }

        // Should show empty text fields
        composeTestRule.onNodeWithText("Заголовок").assertIsDisplayed()
        composeTestRule.onNodeWithText("Содержание").assertIsDisplayed()
    }
}
