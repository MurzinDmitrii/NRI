package com.example.nri.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

@Dao
interface TabletCardDao {
    @Query("""
        SELECT * FROM tablet_cards 
        ORDER BY id DESC
    """)
    fun getAll(): Flow<List<TabletCard>>

    @Query("SELECT * FROM tablet_cards WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TabletCard?

    @Query("""
        SELECT * FROM tablet_cards WHERE bagItemId = :bagItemId LIMIT 1
    """)
    suspend fun getByBagItemId(bagItemId: Long): TabletCard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tabletCard: TabletCard): Long

    @Update
    suspend fun update(tabletCard: TabletCard)

    @Delete
    suspend fun delete(tabletCard: TabletCard)

    @Query("""
        SELECT COUNT(*) FROM tablet_cards
    """)
    fun getCount(): Flow<Int>

    @Transaction
    @Query("""
        SELECT tc.*, bi.name, bi.description, bi.type, bi.uses as bagItemUses
        FROM tablet_cards tc
        INNER JOIN bag_items bi ON tc.bagItemId = bi.id
        ORDER BY tc.id DESC
    """)
    fun getAllWithBagItem(): Flow<List<TabletCardWithBagItem>>
}
