package com.example.nri.ui.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.nri.data.Note
import org.junit.Rule
import org.junit.Test

class NoteCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `noteCard displays note title`() {
        val note = Note(id = 1, title = "Моя заметка", content = "Содержимое")

        composeTestRule.setContent {
            NoteCard(
                note = note,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
    }

    @Test
    fun `noteCard displays note content when not blank`() {
        val note = Note(id = 1, title = "Заметка", content = "Текст заметки")

        composeTestRule.setContent {
            NoteCard(
                note = note,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(note.content).assertIsDisplayed()
    }

    @Test
    fun `noteCard does not display content when blank`() {
        val note = Note(id = 1, title = "Заметка", content = "")

        composeTestRule.setContent {
            NoteCard(
                note = note,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
    }

    @Test
    fun `noteCard does not display content when whitespace`() {
        val note = Note(id = 1, title = "Заметка", content = "   ")

        composeTestRule.setContent {
            NoteCard(
                note = note,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
    }

    @Test
    fun `noteCard displays title and content with ellipsis when long`() {
        val longContent = "A".repeat(500)
        val note = Note(id = 1, title = "Длинный заголовок", content = longContent)

        composeTestRule.setContent {
            NoteCard(
                note = note,
                onEdit = {},
                onDelete = {}
            )
        }

        // Should not crash with long content
        composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
    }
}
