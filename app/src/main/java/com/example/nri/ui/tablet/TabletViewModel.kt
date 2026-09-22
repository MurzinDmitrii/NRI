package com.example.nri.ui.tablet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.BagItem
import com.example.nri.data.BagRepository
import com.example.nri.data.TabletCardRepository
import com.example.nri.data.TabletCardWithBagItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TabletViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val repository = TabletCardRepository(db.tabletCardDao())
    private val bagRepository = BagRepository(db.bagItemDao())

    val tabletCards: StateFlow<List<TabletCardWithBagItem>> = repository.tabletCards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _maxCards = MutableStateFlow(3)
    val maxCards: StateFlow<Int> = _maxCards

    val currentCount: StateFlow<Int> = repository.count
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val availableCardItems: StateFlow<List<BagItem>> = bagRepository.cardItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setMaxCards(n: Int) {
        _maxCards.value = n.coerceAtLeast(1)
    }

    fun addCard(bagItemId: Long) {
        viewModelScope.launch {
            repository.add(bagItemId)
        }
    }

    fun removeCard(tabletCardId: Long) {
        viewModelScope.launch {
            repository.removeById(tabletCardId)
        }
    }

    fun changeUses(bagItem: BagItem, delta: Int) {
        val currentUses = bagItem.uses ?: 0
        val newUses = (currentUses + delta).coerceAtLeast(0)
        viewModelScope.launch {
            bagRepository.updateUses(bagItem.id, newUses)
        }
    }
}
