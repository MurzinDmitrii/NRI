package com.example.nri.ui.bag

import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import com.example.nri.data.WeaponType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BagItemDialogLogicTest {

    @Test
    fun createBagItem_from_valid_form_values_returns_non_null_item() {
        // Given valid form values
        val name = "Меч"
        val description = "Острый меч"
        val type = BagItemType.WEAPON
        val quantity = 1
        val weaponType = WeaponType.ONE_HANDED
        val damage = "1d8+3"

        // When creating BagItem
        val item = createBagItem(
            name = name,
            description = description,
            type = type,
            quantity = quantity,
            weaponType = weaponType,
            damage = damage,
            armorClass = null,
            uses = null
        )

        // Then item is correctly created
        assertEquals(name, item.name)
        assertEquals(description, item.description)
        assertEquals(type, item.type)
        assertEquals(quantity, item.quantity)
        assertEquals(weaponType, item.weaponType)
        assertEquals(damage, item.damage)
        assertNull(item.armorClass)
        assertNull(item.uses)
    }

    @Test
    fun createBagItem_with_armor_type_sets_armorClass() {
        val item = createBagItem(
            name = "Доспех",
            description = "Тяжёлый доспех",
            type = BagItemType.ARMOR,
            quantity = 1,
            weaponType = null,
            damage = null,
            armorClass = 18,
            uses = null
        )

        assertEquals(BagItemType.ARMOR, item.type)
        assertEquals(18, item.armorClass)
        assertNull(item.weaponType)
        assertNull(item.damage)
    }

    @Test
    fun createBagItem_with_card_type_sets_uses() {
        val item = createBagItem(
            name = "Карта огня",
            description = "Огненный шар",
            type = BagItemType.CARD,
            quantity = 1,
            weaponType = null,
            damage = null,
            armorClass = null,
            uses = 5
        )

        assertEquals(BagItemType.CARD, item.type)
        assertEquals(5, item.uses)
        assertNull(item.weaponType)
        assertNull(item.damage)
        assertNull(item.armorClass)
    }

    @Test
    fun createBagItem_with_MISC_type_has_no_special_fields() {
        val item = createBagItem(
            name = "Зелье",
            description = "Лечебное зелье",
            type = BagItemType.MISC,
            quantity = 3,
            weaponType = null,
            damage = null,
            armorClass = null,
            uses = null
        )

        assertEquals(BagItemType.MISC, item.type)
        assertEquals(3, item.quantity)
        assertNull(item.weaponType)
        assertNull(item.damage)
        assertNull(item.armorClass)
        assertNull(item.uses)
    }

    @Test
    fun quantity_coerced_to_minimum_1_when_zero() {
        val item = createBagItem(
            name = "Вещь",
            description = "",
            type = BagItemType.MISC,
            quantity = 0,
            weaponType = null,
            damage = null,
            armorClass = null,
            uses = null
        )

        assertEquals(1, item.quantity)
    }

    @Test
    fun quantity_coerced_to_minimum_1_when_negative() {
        val item = createBagItem(
            name = "Вещь",
            description = "",
            type = BagItemType.MISC,
            quantity = -5,
            weaponType = null,
            damage = null,
            armorClass = null,
            uses = null
        )

        assertEquals(1, item.quantity)
    }

    @Test
    fun damage_is_null_when_empty_string_for_non_weapon_type() {
        val item = createBagItem(
            name = "Щит",
            description = "",
            type = BagItemType.ARMOR,
            quantity = 1,
            weaponType = null,
            damage = "",
            armorClass = 15,
            uses = null
        )

        // For non-WEAPON types, damage should be null regardless of value
        assertNull(item.damage)
    }

    @Test
    fun weaponType_is_null_for_non_weapon_type() {
        val item = createBagItem(
            name = "Зелье",
            description = "",
            type = BagItemType.POTION,
            quantity = 1,
            weaponType = WeaponType.TWO_HANDED,
            damage = null,
            armorClass = null,
            uses = null
        )

        // For non-WEAPON types, weaponType should be null
        assertNull(item.weaponType)
    }

    @Test
    fun armorClass_is_null_for_non_armor_type() {
        val item = createBagItem(
            name = "Меч",
            description = "",
            type = BagItemType.WEAPON,
            quantity = 1,
            weaponType = WeaponType.ONE_HANDED,
            damage = "1d8+2",
            armorClass = 10,
            uses = null
        )

        // For non-ARMOR types, armorClass should be null
        assertNull(item.armorClass)
    }

    @Test
    fun uses_is_null_for_non_card_type() {
        val item = createBagItem(
            name = "Зелье",
            description = "",
            type = BagItemType.POTION,
            quantity = 2,
            weaponType = null,
            damage = null,
            armorClass = null,
            uses = 10
        )

        // For non-CARD types, uses should be null
        assertNull(item.uses)
    }

    @Test
    fun trim_removes_whitespace_from_name_and_description() {
        val item = createBagItem(
            name = "  Меч  ",
            description = "  Острый меч  ",
            type = BagItemType.WEAPON,
            quantity = 1,
            weaponType = WeaponType.ONE_HANDED,
            damage = "  1d8+3  ",
            armorClass = null,
            uses = null
        )

        assertEquals("Меч", item.name)
        assertEquals("Острый меч", item.description)
        assertEquals("1d8+3", item.damage)
    }

    @Test
    fun empty_damage_string_becomes_null() {
        val item = createBagItem(
            name = "Меч",
            description = "",
            type = BagItemType.WEAPON,
            quantity = 1,
            weaponType = WeaponType.ONE_HANDED,
            damage = "   ",
            armorClass = null,
            uses = null
        )

        assertNull(item.damage)
    }

    private fun createBagItem(
        name: String,
        description: String,
        type: BagItemType,
        quantity: Int,
        weaponType: WeaponType?,
        damage: String?,
        armorClass: Int?,
        uses: Int?
    ): BagItem {
        return BagItem(
            id = 0,
            name = name.trim(),
            description = description.trim(),
            type = type,
            quantity = quantity.coerceAtLeast(1),
            weaponType = if (type == BagItemType.WEAPON) weaponType else null,
            damage = if (type == BagItemType.WEAPON) damage?.trim().takeIf { !it.isNullOrBlank() } else null,
            armorClass = if (type == BagItemType.ARMOR) armorClass else null,
            uses = if (type == BagItemType.CARD) uses else null,
        )
    }
}
