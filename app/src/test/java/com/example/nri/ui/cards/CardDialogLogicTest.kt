package com.example.nri.ui.cards

import com.example.nri.data.Card
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardDialogLogicTest {

    @Test
    fun `createCard from valid form values returns item with trimmed name and description`() {
        val card = createCard(
            name = "  Карта Огня  ",
            description = "  Огненный шар  "
        )

        assertEquals("Карта Огня", card.name)
        assertEquals("Огненный шар", card.description)
    }

    @Test
    fun `createCard preserves name when already trimmed`() {
        val card = createCard(name = "Ледяная стрела", description = "")

        assertEquals("Ледяная стрела", card.name)
        assertEquals("", card.description)
    }

    @Test
    fun `createCard with id 0 for new cards`() {
        val card = createCard(name = "Новая", description = "")

        assertEquals(0L, card.id)
    }

    @Test
    fun `createCard with existing id preserves it`() {
        val card = createCard(id = 42, name = "Существующая", description = "")

        assertEquals(42L, card.id)
    }

    @Test
    fun `name must not be blank`() {
        val blankCard = createCard(name = "", description = "")
        assertTrue(blankCard.name.isBlank())

        val whitespaceCard = createCard(name = "   ", description = "")
        assertTrue(whitespaceCard.name.isBlank())
    }

    @Test
    fun `description can be empty`() {
        val card = createCard(name = "Карта", description = "")

        assertEquals("", card.description)
        assertTrue(card.description.isBlank())
    }

    @Test
    fun `description can be non-blank`() {
        val card = createCard(name = "Карта", description = "Описание карты")

        assertEquals("Описание карты", card.description)
        assertFalse(card.description.isBlank())
    }

    private fun createCard(
        id: Long = 0,
        name: String,
        description: String
    ): Card {
        return Card(
            id = id,
            name = name.trim(),
            description = description.trim()
        )
    }
}
