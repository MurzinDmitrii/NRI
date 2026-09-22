package com.example.nri.data

import androidx.room.Embedded
import androidx.room.Relation

data class TabletCardWithBagItem(
    @Embedded val tabletCard: TabletCard,
    @Relation(
        entity = BagItem::class,
        parentColumn = "bagItemId",
        entityColumn = "id"
    )
    val bagItem: BagItem
)
