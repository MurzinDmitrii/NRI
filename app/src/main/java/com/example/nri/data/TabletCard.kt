package com.example.nri.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tablet_cards",
    foreignKeys = [
        ForeignKey(
            entity = BagItem::class,
            parentColumns = ["id"],
            childColumns = ["bagItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bagItemId")]
)
data class TabletCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bagItemId: Long
)
