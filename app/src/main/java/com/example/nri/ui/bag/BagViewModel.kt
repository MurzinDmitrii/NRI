package com.example.nri.ui.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.BagItem
import com.example.nri.data.BagRepository
import com.example.nri.data.CardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.nri.data.Card

class BagViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val bagRepository = BagRepository(db.bagItemDao())
    private val cardRepository = CardRepository(db.cardDao())
    val items: StateFlow<List<BagItem>> = bagRepository.items
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cards: StateFlow<List<Card>> = cardRepository.cards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cardItems: StateFlow<List<BagItem>> = bagRepository.cardItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(item: BagItem) {
        viewModelScope.launch {
            if (item.id == 0L) bagRepository.add(item) else bagRepository.update(item)
        }
    }

    fun delete(item: BagItem) {
        viewModelScope.launch { bagRepository.delete(item) }
    }

    fun changeQuantity(item: BagItem, delta: Int) {
        val newQty = (item.quantity + delta).coerceAtLeast(1)
        if (newQty == item.quantity) return
        viewModelScope.launch {
            bagRepository.updateQuantity(item.id, newQty)
        }
    }
}