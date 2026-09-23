package com.example.nri.ui.tablet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import org.junit.Rule
import org.junit.Test

class AddCardDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `dialog shows title Добавить карту на планшет`() {
        composeTestRule.setContent {
            AddCardDialog(
                availableCards = emptyList(),
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Добавить карту на планшет").assertIsDisplayed()
    }

    @Test
    fun `dialog shows available slots count`() {
        composeTestRule.setContent {
            AddCardDialog(
                availableCards = emptyList(),
                existingBagItemIds = emptyList(),
                maxSlots = 5,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Доступно слотов: 5").assertIsDisplayed()
    }

    @Test
    fun `dialog shows message when all cards already added`() {
        composeTestRule.setContent {
            AddCardDialog(
                availableCards = emptyList(),
                existingBagItemIds = listOf(1L, 2L),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Все карты из инвентаря уже добавлены на планшет.").assertIsDisplayed()
    }

    @Test
    fun `dialog shows dropdown when cards available`() {
        val availableCards = listOf(
            BagItem(id = 1, name = "Карта огня", description = "", type = BagItemType.CARD, uses = 5),
            BagItem(id = 2, name = "Карта льда", description = "", type = BagItemType.CARD, uses = null)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = availableCards,
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Выберите карту").assertIsDisplayed()
    }

    @Test
    fun `dialog shows selected item with uses`() {
        val availableCards = listOf(
            BagItem(id = 1, name = "Карта огня", description = "", type = BagItemType.CARD, uses = 5)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = availableCards,
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = 1,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Карта огня - 5 использований").assertIsDisplayed()
    }

    @Test
    fun `dialog shows infinity for item with null uses`() {
        val availableCards = listOf(
            BagItem(id = 1, name = "Карта ветра", description = "", type = BagItemType.CARD, uses = null)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = availableCards,
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = 1,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Карта ветра - ∞ использований").assertIsDisplayed()
    }

    @Test
    fun `dialog always shows Add and Cancel buttons`() {
        composeTestRule.setContent {
            AddCardDialog(
                availableCards = emptyList(),
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Добавить").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отмена").assertIsDisplayed()
    }

    @Test
    fun `onDismiss called when Cancel clicked`() {
        var dismissed = false

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = emptyList(),
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = { dismissed = true },
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Отмена").performClick()

        assert(dismissed)
    }

    @Test
    fun `dropdown shows available cards`() {
        val availableCards = listOf(
            BagItem(id = 1, name = "Карта огня", description = "", type = BagItemType.CARD, uses = 5),
            BagItem(id = 2, name = "Карта льда", description = "", type = BagItemType.CARD, uses = 3)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = availableCards,
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Open dropdown
        composeTestRule.onNodeWithText("Выберите карту").performClick()

        composeTestRule.onNodeWithText("Карта огня - 5 использований").assertIsDisplayed()
        composeTestRule.onNodeWithText("Карта льда - 3 использований").assertIsDisplayed()
    }

    @Test
    fun `filteredCards excludes already added items`() {
        val allCards = listOf(
            BagItem(id = 1, name = "Карта огня", description = "", type = BagItemType.CARD, uses = 5),
            BagItem(id = 2, name = "Карта льда", description = "", type = BagItemType.CARD, uses = 3)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = allCards,
                existingBagItemIds = listOf(1L),
                maxSlots = 3,
                selectedBagItemId = null,
                onSelected = {},
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Open dropdown
        composeTestRule.onNodeWithText("Выберите карту").performClick()

        // Only card 2 should be visible
        composeTestRule.onNodeWithText("Карта льда - 3 использований").assertIsDisplayed()
    }

    @Test
    fun `onConfirm called with selected bagItemId`() {
        var receivedId: Long? = null
        val availableCards = listOf(
            BagItem(id = 42, name = "Карта", description = "", type = BagItemType.CARD, uses = 5)
        )

        composeTestRule.setContent {
            AddCardDialog(
                availableCards = availableCards,
                existingBagItemIds = emptyList(),
                maxSlots = 3,
                selectedBagItemId = 42,
                onSelected = {},
                onDismiss = {},
                onConfirm = { receivedId = it }
            )
        }

        composeTestRule.onNodeWithText("Добавить").performClick()

        assert(receivedId == 42L)
    }
}
