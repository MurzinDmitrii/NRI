package com.example.nri.ui.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType
import com.example.nri.data.CharacterInfoType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class CharacterViewModel(application: Application) : AndroidViewModel(application) {
    private val bagDao = AppDatabase.getInstance(application).bagItemDao()
    private val infoDao = AppDatabase.getInstance(application).characterInfoDao()

    val grams: StateFlow<String> = infoDao
        .getAll()
        .map { list ->
            list.find { it.type == CharacterInfoType.GOLD }?.value ?: "0"
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "0")

    val armorList: StateFlow<List<BagItem>> = bagDao
        .getAll()
        .map { list -> list.filter { it.type == BagItemType.ARMOR } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val selectedArmorName: StateFlow<String> = infoDao
        .getAll()
        .map { list ->
            list.find { it.type == CharacterInfoType.ARMOR_NAME }?.value ?: ""
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val armorClass: StateFlow<String> = infoDao
        .getAll()
        .map { list ->
            list.find { it.type == CharacterInfoType.ARMOR_CLASS }?.value ?: "10"
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "10")

    val health: StateFlow<Int> = infoDao
        .getAll()
        .map { list ->
            list.find { it.type == CharacterInfoType.HEALTH }?.value?.toIntOrNull() ?: 0
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private suspend fun saveInfo(type: CharacterInfoType, value: String) {
        val updated = infoDao.updateValue(type, value)
        if (updated == 0) {
            infoDao.insert(type, value)
        }
    }

    fun saveGrams(value: String) {
        viewModelScope.launch { saveInfo(CharacterInfoType.GOLD, value) }
    }

    fun saveHealth(value: Int) {
        viewModelScope.launch { saveInfo(CharacterInfoType.HEALTH, value.toString()) }
    }

    fun initHealthIfNotSet(maxHealth: Int) {
        viewModelScope.launch {
            val existing = infoDao.getByType(CharacterInfoType.HEALTH)
            if (existing == null) {
                saveInfo(CharacterInfoType.HEALTH, maxHealth.toString())
            }
        }
    }

    fun saveArmorClass(value: String) {
        viewModelScope.launch { saveInfo(CharacterInfoType.ARMOR_CLASS, value) }
    }

    fun saveSelectedArmor(name: String) {
        viewModelScope.launch { saveInfo(CharacterInfoType.ARMOR_NAME, name) }
    }

    fun clearArmor() {
        viewModelScope.launch {
            saveInfo(CharacterInfoType.ARMOR_CLASS, "10")
            saveInfo(CharacterInfoType.ARMOR_NAME, "")
        }
    }
}
