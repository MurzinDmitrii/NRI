package com.example.nri.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BeltItemRepository(private val dao: BeltItemDao) {

    val beltItems: Flow<List<BeltItemWithBagItem>> = dao.getAllWithBagItem()

    val count: Flow<Int> = dao.getCount()

    suspend fun add(bagItemId: Long) {
        val existing = dao.getByBagItemId(bagItemId)
        if (existing == null) {
            dao.insert(BeltItem(bagItemId = bagItemId))
        }
    }

    suspend fun remove(beltItem: BeltItem) {
        dao.delete(beltItem)
    }

    suspend fun removeById(id: Long) {
        dao.getById(id)?.let { dao.delete(it) }
    }

    suspend fun getByBagItemId(bagItemId: Long): BeltItem? {
        return dao.getByBagItemId(bagItemId)
    }
}
