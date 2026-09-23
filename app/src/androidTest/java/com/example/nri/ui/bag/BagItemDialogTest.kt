package com.example.nri.ui.bag

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import com.example.nri.data.Card
import com.example.nri.data.WeaponType
import org.junit.Rule
import org.junit.Test

class BagItemDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `new dialog shows title Новая вещь`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Новая вещь").assertIsDisplayed()
    }

    @Test
    fun `edit dialog shows title Редактировать`() {
        val existingItem = BagItem(
            id = 1,
            name = "Меч",
            description = "Острый",
            type = BagItemType.WEAPON,
            weaponType = WeaponType.ONE_HANDED,
            damage = "1d8+2",
            quantity = 1
        )

        composeTestRule.setContent {
            BagItemDialog(
                initial = existingItem,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Редактировать").assertIsDisplayed()
    }

    @Test
    fun `new dialog pre-fills type with MISC`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).assertIsDisplayed()
    }

    @Test
    fun `edit dialog pre-fills with existing item values`() {
        val existingItem = BagItem(
            id = 1,
            name = "Стальной щит",
            description = "Прочный щит",
            type = BagItemType.ARMOR,
            armorClass = 17,
            quantity = 2
        )

        composeTestRule.setContent {
            BagItemDialog(
                initial = existingItem,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Стальной щит").assertIsDisplayed()
        composeTestRule.onNodeWithText("Прочный щит").assertIsDisplayed()
        composeTestRule.onNodeWithText(BagItemType.ARMOR.displayName).assertIsDisplayed()
    }

    @Test
    fun `dialog always shows Save and Cancel buttons`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Сохранить").assertIsDisplayed()
        composeTestRule.onNodeWithText("Отмена").assertIsDisplayed()
    }

    @Test
    fun `type dropdown shows all entries`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Click the type dropdown to expand it
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()

        // Verify all types are visible
        BagItemType.entries.forEach { type ->
            composeTestRule.onNodeWithText(type.displayName).assertIsDisplayed()
        }
    }

    @Test
    fun `weapon dialog shows weapon type and damage fields`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Select WEAPON type
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()
        composeTestRule.onNodeWithText(BagItemType.WEAPON.displayName).performClick()

        composeTestRule.onNodeWithText("Тип оружия").assertIsDisplayed()
        composeTestRule.onNodeWithText("Урон (например, 1d6+2)").assertIsDisplayed()
    }

    @Test
    fun `armor dialog shows armor class field`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Select ARMOR type
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()
        composeTestRule.onNodeWithText(BagItemType.ARMOR.displayName).performClick()

        composeTestRule.onNodeWithText("КД (класс доспеха)").assertIsDisplayed()
    }

    @Test
    fun `card dialog shows card selection dropdown`() {
        val testCards = listOf(
            Card(id = 1, name = "Карта огня", description = "Огненный шар"),
            Card(id = 2, name = "Карта льда", description = "Ледяная стрела")
        )

        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = testCards,
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Select CARD type
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()
        composeTestRule.onNodeWithText(BagItemType.CARD.displayName).performClick()

        composeTestRule.onNodeWithText("Карта").assertIsDisplayed()

        // Open card dropdown
        composeTestRule.onNodeWithText("Карта огня").performClick()

        // Verify cards are listed
        composeTestRule.onNodeWithText("Карта огня").assertIsDisplayed()
        composeTestRule.onNodeWithText("Карта льда").assertIsDisplayed()
    }

    @Test
    fun `card dialog shows message when no cards available`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Select CARD type
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()
        composeTestRule.onNodeWithText(BagItemType.CARD.displayName).performClick()

        // Open card dropdown
        composeTestRule.onNodeWithText("").performClick()

        composeTestRule.onNodeWithText("Сначала добавьте карты в Архив").assertIsDisplayed()
    }

    @Test
    fun `card type shows uses field`() {
        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        // Select CARD type
        composeTestRule.onNodeWithText(BagItemType.MISC.displayName).performClick()
        composeTestRule.onNodeWithText(BagItemType.CARD.displayName).performClick()

        composeTestRule.onNodeWithText("Количество использований").assertIsDisplayed()
    }

    @Test
    fun `onConfirm called with correct BagItem when Save clicked`() {
        var receivedItem: BagItem? = null

        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = { receivedItem = it }
            )
        }

        composeTestRule.onNodeWithText("Сохранить").performClick()

        assert(receivedItem != null)
        assert(receivedItem!!.name.isNotBlank())
    }

    @Test
    fun `onDismiss called when Cancel clicked`() {
        var dismissed = false

        composeTestRule.setContent {
            BagItemDialog(
                initial = null,
                cards = emptyList(),
                onDismiss = { dismissed = true },
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Отмена").performClick()

        assert(dismissed)
    }

    @Test
    fun `weapon dialog pre-fills weapon type and damage`() {
        val existingItem = BagItem(
            id = 1,
            name = "Меч",
            description = "",
            type = BagItemType.WEAPON,
            weaponType = WeaponType.TWO_HANDED,
            damage = "2d6+1",
            quantity = 1
        )

        composeTestRule.setContent {
            BagItemDialog(
                initial = existingItem,
                cards = emptyList(),
                onDismiss = {},
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText(WeaponType.TWO_HANDED.displayName).assertIsDisplayed()
    }
}
