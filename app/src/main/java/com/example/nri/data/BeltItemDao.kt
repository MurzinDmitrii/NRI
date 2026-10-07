package com.example.nri.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

@Dao
interface BeltItemDao {
    @Query("""
        SELECT * FROM belt_items 
        ORDER BY id DESC
    """)
    fun getAll(): Flow<List<BeltItem>>

    @Query("SELECT * FROM belt_items WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): BeltItem?

    @Query("""
        SELECT * FROM belt_items WHERE bagItemId = :bagItemId LIMIT 1
    """)
    suspend fun getByBagItemId(bagItemId: Long): BeltItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beltItem: BeltItem): Long

    @Update
    suspend fun update(beltItem: BeltItem)

    @Delete
    suspend fun delete(beltItem: BeltItem)

    @Query("""
        SELECT COUNT(*) FROM belt_items
    """)
    fun getCount(): Flow<Int>

    @Transaction
    @Query("""
        SELECT bi.*, bag.name, bag.description, bag.type, bag.uses as bagItemUses
        FROM belt_items bi
        INNER JOIN bag_items bag ON bi.bagItemId = bag.id
        ORDER BY bi.id DESC
    """)
    fun getAllWithBagItem(): Flow<List<BeltItemWithBagItem>>
}
