package com.example.nri.data

import kotlinx.coroutines.flow.Flow

class CardRepository(private val dao: CardDao) {
    val cards: Flow<List<Card>> = dao.getAll()
    suspend fun add(card: Card) = dao.insert(card)
    suspend fun update(card: Card) = dao.update(card)
    suspend fun delete(card: Card) = dao.delete(card)
}