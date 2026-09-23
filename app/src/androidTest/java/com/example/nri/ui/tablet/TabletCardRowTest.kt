package com.example.nri.ui.tablet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import org.junit.Rule
import org.junit.Test

class TabletCardRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `tabletCardRow displays bag item name`() {
        val bagItem = BagItem(id = 1, name = "Карта огня", description = "Огненный шар", type = BagItemType.CARD, uses = 5)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText(bagItem.name).assertIsDisplayed()
    }

    @Test
    fun `tabletCardRow displays description when not blank`() {
        val bagItem = BagItem(id = 1, name = "Карта льда", description = "Ледяная стрела", type = BagItemType.CARD, uses = 3)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText(bagItem.description).assertIsDisplayed()
    }

    @Test
    fun `tabletCardRow displays uses count when positive`() {
        val bagItem = BagItem(id = 1, name = "Карта молнии", description = "", type = BagItemType.CARD, uses = 7)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText("Использований: 7").assertIsDisplayed()
    }

    @Test
    fun `tabletCardRow displays infinity when uses is null`() {
        val bagItem = BagItem(id = 1, name = "Карта ветра", description = "", type = BagItemType.CARD, uses = null)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText("Использований: ∞").assertIsDisplayed()
    }

    @Test
    fun `tabletCardRow displays infinity when uses is zero`() {
        val bagItem = BagItem(id = 1, name = "Карта", description = "", type = BagItemType.CARD, uses = 0)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText("Использований: ∞").assertIsDisplayed()
    }

    @Test
    fun `tabletCardRow does not display description when blank`() {
        val bagItem = BagItem(id = 1, name = "Простая карта", description = "", type = BagItemType.CARD, uses = 1)

        composeTestRule.setContent {
            TabletCardRow(
                bagItem = bagItem,
                onRemove = {},
                onUsesChange = {}
            )
        }

        composeTestRule.onNodeWithText(bagItem.name).assertIsDisplayed()
    }
}
