package com.example.nri.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TabletCardRepository(private val dao: TabletCardDao) {

    val tabletCards: Flow<List<TabletCardWithBagItem>> = dao.getAllWithBagItem()

    val count: Flow<Int> = dao.getCount()

    suspend fun add(bagItemId: Long) {
        val existing = dao.getByBagItemId(bagItemId)
        if (existing == null) {
            dao.insert(TabletCard(bagItemId = bagItemId))
        }
    }

    suspend fun remove(tabletCard: TabletCard) {
        dao.delete(tabletCard)
    }

    suspend fun removeById(id: Long) {
        dao.getById(id)?.let { dao.delete(it) }
    }

    suspend fun getByBagItemId(bagItemId: Long): TabletCard? {
        return dao.getByBagItemId(bagItemId)
    }
}
