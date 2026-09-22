package com.example.nri.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для хранения характеристик и подхарактеристик
 */
@Entity(tableName = "characteristics")
data class Characteristic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: CharacteristicType,
    val value: Int,
    val progress: Int
)

/**
 * Сущность для хранения подхарактеристик
 */
@Entity(tableName = "sub_characteristics")
data class SubCharacteristic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: SubCharacteristicType,
    val value: Int,
    val progress: Int
)
