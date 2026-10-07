package com.example.nri.data

import androidx.room.Embedded
import androidx.room.Relation

data class BeltItemWithBagItem(
    @Embedded val beltItem: BeltItem,
    @Relation(
        entity = BagItem::class,
        parentColumn = "bagItemId",
        entityColumn = "id"
    )
    val bagItem: BagItem
)
