package com.example.nri.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bag_items")
data class BagItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val type: BagItemType,
    val weaponType: WeaponType? = null,
    val damage: String? = null,
    val armorClass: Int? = null,
    val quantity: Int = 1,
    val uses: Int? = null,
)