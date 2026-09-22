package com.example.nri.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BagItemDao {
    @Query("SELECT * FROM bag_items ORDER BY id DESC")
    fun getAll(): Flow<List<BagItem>>

    @Query("SELECT * FROM bag_items WHERE id = :id")
    suspend fun getById(id: Long): BagItem?

    @Insert
    suspend fun insert(item: BagItem): Long

    @Update
    suspend fun update(item: BagItem)

    @Delete
    suspend fun delete(item: BagItem)

    @Query("DELETE FROM bag_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE bag_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("SELECT * FROM bag_items WHERE type = 'CARD' ORDER BY id DESC")
    fun getAllCards(): Flow<List<BagItem>>

    @Query("UPDATE bag_items SET uses = :uses WHERE id = :id")
    suspend fun updateUses(id: Long, uses: Int)
}