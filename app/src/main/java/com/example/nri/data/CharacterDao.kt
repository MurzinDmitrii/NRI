package com.example.nri.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAll(): Flow<List<Character>>

    @Insert
    suspend fun insert(character: Character)

    @Delete
    suspend fun delete(character: Character)
}