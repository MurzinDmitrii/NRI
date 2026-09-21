package com.example.nri.ui.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.Card
import com.example.nri.data.CardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardArchiveViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = CardRepository(AppDatabase.getInstance(app).cardDao())

    val cards: StateFlow<List<Card>> = repository.cards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(card: Card) {
        viewModelScope.launch {
            if (card.id == 0L) repository.add(card) else repository.update(card)
        }
    }

    fun delete(card: Card) {
        viewModelScope.launch { repository.delete(card) }
    }
}