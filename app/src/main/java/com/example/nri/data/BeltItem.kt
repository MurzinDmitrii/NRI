package com.example.nri.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "belt_items",
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
data class BeltItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bagItemId: Long
)
