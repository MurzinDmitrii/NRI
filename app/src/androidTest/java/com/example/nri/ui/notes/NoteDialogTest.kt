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
    fun `dialog shows custom title`() {
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
    fun `dialog shows label Заголовок`() {
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
    fun `dialog shows label Содержание`() {
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
    fun `dialog pre-fills title and content`() {
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
    fun `dialog always shows Save and Cancel buttons`() {
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
    fun `onDismiss called when Cancel clicked`() {
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
    fun `onConfirm called with title and content when Save clicked`() {
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
    fun `new dialog pre-fills with empty values`() {
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
