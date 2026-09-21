package com.example.nri.ui.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.BagItem
import com.example.nri.data.BagRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BagViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = BagRepository(AppDatabase.getInstance(app).bagItemDao())

    val items: StateFlow<List<BagItem>> = repository.items
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(item: BagItem) {
        viewModelScope.launch {
            if (item.id == 0L) repository.add(item) else repository.update(item)
        }
    }

    fun delete(item: BagItem) {
        viewModelScope.launch { repository.delete(item) }
    }

    fun changeQuantity(item: BagItem, delta: Int) {
        val newQty = (item.quantity + delta).coerceAtLeast(1)
        if (newQty == item.quantity) return
        viewModelScope.launch {
            repository.updateQuantity(item.id, newQty)
        }
    }
}