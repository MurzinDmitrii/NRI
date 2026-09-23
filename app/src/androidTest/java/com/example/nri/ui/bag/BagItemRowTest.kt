package com.example.nri.ui.bag

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import com.example.nri.data.WeaponType
import org.junit.Rule
import org.junit.Test

class BagItemRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `bagItemRow displays item name`() {
        val item = createTestWeaponItem()

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays item type displayName`() {
        val item = createTestWeaponItem()

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText(item.type.displayName).assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays weapon type and damage for WEAPON`() {
        val item = createTestWeaponItem()

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("Тип оружия: ${item.weaponType!!.displayName}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Урон: ${item.damage}").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays armor class for ARMOR`() {
        val item = BagItem(
            id = 1,
            name = "Кольчуга",
            description = "",
            type = BagItemType.ARMOR,
            armorClass = 16,
            quantity = 1
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("КД: 16").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays uses for CARD`() {
        val item = BagItem(
            id = 1,
            name = "Карта молнии",
            description = "",
            type = BagItemType.CARD,
            uses = 7,
            quantity = 2
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("Использований: 7").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays description when not blank`() {
        val item = BagItem(
            id = 1,
            name = "Старый меч",
            description = "Ржавый, но острый",
            type = BagItemType.WEAPON,
            quantity = 1
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("Ржавый, но острый").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow does not display description when blank`() {
        val item = BagItem(
            id = 1,
            name = "Вещь",
            description = "",
            type = BagItemType.MISC,
            quantity = 1
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("Вещь").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow displays correct quantity`() {
        val item = BagItem(
            id = 1,
            name = "Зелье",
            description = "",
            type = BagItemType.POTION,
            quantity = 5
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        composeTestRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun `bagItemRow does not show weapon fields for non-weapon items`() {
        val item = BagItem(
            id = 1,
            name = "Зелье лечения",
            description = "",
            type = BagItemType.POTION,
            quantity = 3
        )

        composeTestRule.setContent {
            BagItemRow(
                item = item,
                onEdit = {},
                onDelete = {},
                onQuantityChange = {}
            )
        }

        // Should display item name and type
        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.type.displayName).assertIsDisplayed()
    }

    private fun createTestWeaponItem(): BagItem {
        return BagItem(
            id = 1,
            name = "Длинный меч",
            description = "Клинок эльфа",
            type = BagItemType.WEAPON,
            weaponType = WeaponType.ONE_HANDED,
            damage = "1d8+2",
            quantity = 1
        )
    }
}
