package com.example.nri.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterInfoDao {
    @Query("SELECT * FROM character_info ORDER BY id ASC")
    fun getAll(): Flow<List<CharacterInfo>>

    @Query("SELECT * FROM character_info WHERE type = :type LIMIT 1")
    suspend fun getByType(type: CharacterInfoType): CharacterInfo?

    @Query("UPDATE character_info SET value = :value WHERE type = :type")
    suspend fun updateValue(type: CharacterInfoType, value: String): Int

    @Query("INSERT INTO character_info (type, value) VALUES (:type, :value)")
    suspend fun insert(type: CharacterInfoType, value: String)

    @Query("DELETE FROM character_info WHERE type = :type")
    suspend fun deleteByType(type: CharacterInfoType)
}
