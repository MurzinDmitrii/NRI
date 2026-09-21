package com.example.nri.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY name ASC")
    fun getAll(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Card?

    @Insert
    suspend fun insert(card: Card): Long

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)
}