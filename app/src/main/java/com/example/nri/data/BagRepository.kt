package com.example.nri.data

import kotlinx.coroutines.flow.Flow

class BagRepository(private val dao: BagItemDao) {
    val items: Flow<List<BagItem>> = dao.getAll()
    val cardItems: Flow<List<BagItem>> = dao.getAllCards()

    suspend fun add(item: BagItem) = dao.insert(item)
    suspend fun update(item: BagItem) = dao.update(item)
    suspend fun delete(item: BagItem) = dao.delete(item)
    suspend fun updateQuantity(id: Long, quantity: Int) = dao.updateQuantity(id, quantity)
    suspend fun updateUses(id: Long, uses: Int) = dao.updateUses(id, uses)
}