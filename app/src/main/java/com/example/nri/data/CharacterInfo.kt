package com.example.nri.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для хранения информации персонажа
 */
@Entity(tableName = "character_info")
data class CharacterInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: CharacterInfoType,
    val value: String
)
