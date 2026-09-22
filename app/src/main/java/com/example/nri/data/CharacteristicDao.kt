package com.example.nri.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacteristicDao {
    @Query("SELECT * FROM characteristics ORDER BY id ASC")
    fun getAll(): Flow<List<Characteristic>>

    @Query("SELECT * FROM characteristics WHERE type = :type LIMIT 1")
    suspend fun getByType(type: CharacteristicType): Characteristic?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChar(entity: Characteristic)

    @Query("SELECT * FROM sub_characteristics ORDER BY id ASC")
    fun getAllSub(): Flow<List<SubCharacteristic>>

    @Query("SELECT * FROM sub_characteristics WHERE type = :type LIMIT 1")
    suspend fun getSubByType(type: SubCharacteristicType): SubCharacteristic?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSub(entity: SubCharacteristic)
}
