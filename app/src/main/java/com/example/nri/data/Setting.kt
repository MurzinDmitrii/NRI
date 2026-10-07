package com.example.nri.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey val key: String,
    val valueInt: Int? = null,
    val valueString: String? = null
)
