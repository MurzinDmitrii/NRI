package com.example.nri.ui.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.*
import com.example.nri.data.calculateMainCharAdvantage
import com.example.nri.data.calculateSubCharAdvantage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted

/**
 * Отображение подхарактеристик на основные характеристики
 */
private val subToParent = mapOf(
    SubCharacteristicType.ENDURANCE to CharacteristicType.STRENGTH,
    SubCharacteristicType.ATHLETICS to CharacteristicType.STRENGTH,
    SubCharacteristicType.RESILIENCE to CharacteristicType.STRENGTH,
    SubCharacteristicType.SPEED to CharacteristicType.AGILITY,
    SubCharacteristicType.EVASION to CharacteristicType.AGILITY,
    SubCharacteristicType.ACCURACY to CharacteristicType.AGILITY,
    SubCharacteristicType.TACTICS to CharacteristicType.INTELLIGENCE,
    SubCharacteristicType.WISDOM to CharacteristicType.INTELLIGENCE,
    SubCharacteristicType.PERCEPTION to CharacteristicType.INTELLIGENCE
)

class CharacterStatsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).characteristicDao()

    private fun initChar(type: CharacteristicType, defaultValue: Int): MutableStateFlow<CharacteristicData> {
        val flow = MutableStateFlow(CharacteristicData(defaultValue, 0))
        viewModelScope.launch {
            val entity = dao.getByType(type)
            if (entity != null) {
                flow.value = CharacteristicData(entity.value, entity.progress)
            } else {
                dao.upsertChar(Characteristic(0, type, defaultValue, 0))
            }
        }
        return flow
    }

    private fun initSub(type: SubCharacteristicType, defaultValue: Int = 0): MutableStateFlow<SubCharacteristicData> {
        val flow = MutableStateFlow(SubCharacteristicData(0, 0))
        viewModelScope.launch {
            val entity = dao.getSubByType(type)
            if (entity != null) {
                flow.value = SubCharacteristicData(entity.value, entity.progress)
            } else {
                dao.upsertSub(SubCharacteristic(0, type, defaultValue, 0))
            }
        }
        return flow
    }

    val strength = initChar(CharacteristicType.STRENGTH, 1)
    val agility = initChar(CharacteristicType.AGILITY, 1)
    val intelligence = initChar(CharacteristicType.INTELLIGENCE, 1)

    // Сила
    val endurance = initSub(SubCharacteristicType.ENDURANCE)
    val athletics = initSub(SubCharacteristicType.ATHLETICS)
    val resilience = initSub(SubCharacteristicType.RESILIENCE)

    val maxHealth: StateFlow<Int> = resilience.map { res ->
        10 + (3 * res.value)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 13)

    // Ловкость
    val speed = initSub(SubCharacteristicType.SPEED, 10)
    val evasion = initSub(SubCharacteristicType.EVASION)
    val accuracy = initSub(SubCharacteristicType.ACCURACY)

    // Интеллект
    val tactics = initSub(SubCharacteristicType.TACTICS)
    val wisdom = initSub(SubCharacteristicType.WISDOM)
    val perception = initSub(SubCharacteristicType.PERCEPTION)

    private val _chars = mutableMapOf<CharacteristicType, MutableStateFlow<CharacteristicData>>().apply {
        put(CharacteristicType.STRENGTH, strength)
        put(CharacteristicType.AGILITY, agility)
        put(CharacteristicType.INTELLIGENCE, intelligence)
    }

    private val _subs = mutableMapOf<SubCharacteristicType, MutableStateFlow<SubCharacteristicData>>().apply {
        put(SubCharacteristicType.ENDURANCE, endurance)
        put(SubCharacteristicType.ATHLETICS, athletics)
        put(SubCharacteristicType.RESILIENCE, resilience)
        put(SubCharacteristicType.SPEED, speed)
        put(SubCharacteristicType.EVASION, evasion)
        put(SubCharacteristicType.ACCURACY, accuracy)
        put(SubCharacteristicType.TACTICS, tactics)
        put(SubCharacteristicType.WISDOM, wisdom)
        put(SubCharacteristicType.PERCEPTION, perception)
    }

    // Флаги для показа диалога выбора улучшения
    private val _showUpgradeDialog = MutableStateFlow<Map<CharacteristicType, Boolean>>(emptyMap())
    val showUpgradeDialog: StateFlow<Map<CharacteristicType, Boolean>> = _showUpgradeDialog

    fun clearUpgradeDialog(type: CharacteristicType) {
        _showUpgradeDialog.value = _showUpgradeDialog.value - type
    }

    // Кубы преимущества для основных характеристик
    val strengthDice: StateFlow<DiceType?> = strength.map { calculateMainCharAdvantage(it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val agilityDice: StateFlow<DiceType?> = agility.map { calculateMainCharAdvantage(it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val intelligenceDice: StateFlow<DiceType?> = intelligence.map { calculateMainCharAdvantage(it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Кубы преимущества для подхарактеристик
    val enduranceDice: StateFlow<DiceType?> = endurance.map { calculateSubCharAdvantage(SubCharacteristicType.ENDURANCE, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val athleticsDice: StateFlow<DiceType?> = athletics.map { calculateSubCharAdvantage(SubCharacteristicType.ATHLETICS, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val resilienceDice: StateFlow<DiceType?> = resilience.map { calculateSubCharAdvantage(SubCharacteristicType.RESILIENCE, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val speedDice: StateFlow<DiceType?> = speed.map { calculateSubCharAdvantage(SubCharacteristicType.SPEED, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val evasionDice: StateFlow<DiceType?> = evasion.map { calculateSubCharAdvantage(SubCharacteristicType.EVASION, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val accuracyDice: StateFlow<DiceType?> = accuracy.map { calculateSubCharAdvantage(SubCharacteristicType.ACCURACY, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val tacticsDice: StateFlow<DiceType?> = tactics.map { calculateSubCharAdvantage(SubCharacteristicType.TACTICS, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val wisdomDice: StateFlow<DiceType?> = wisdom.map { calculateSubCharAdvantage(SubCharacteristicType.WISDOM, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    val perceptionDice: StateFlow<DiceType?> = perception.map { calculateSubCharAdvantage(SubCharacteristicType.PERCEPTION, it.value).second }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private fun saveChar(type: CharacteristicType, value: Int, progress: Int) {
        viewModelScope.launch {
            val existing = dao.getByType(type)
            if (existing != null) {
                dao.upsertChar(Characteristic(existing.id, type, value, progress))
            } else {
                dao.upsertChar(Characteristic(0, type, value, progress))
            }
            _chars[type]?.value = CharacteristicData(value, progress)
        }
    }

    private fun saveSub(type: SubCharacteristicType, value: Int, progress: Int) {
        viewModelScope.launch {
            val existing = dao.getSubByType(type)
            if (existing != null) {
                dao.upsertSub(SubCharacteristic(existing.id, type, value, progress))
            } else {
                dao.upsertSub(SubCharacteristic(0, type, value, progress))
            }
            _subs[type]?.value = SubCharacteristicData(value, progress)
        }
    }

    fun incrementValue(type: CharacteristicType) {
        viewModelScope.launch {
            val current = dao.getByType(type) ?: return@launch
            saveChar(type, current.value + 1, current.progress)
        }
    }

    fun decrementValue(type: CharacteristicType) {
        viewModelScope.launch {
            val current = dao.getByType(type) ?: return@launch
            if (current.value > 1) {
                saveChar(type, current.value - 1, current.progress)
            }
        }
    }

    fun incrementProgress(type: CharacteristicType) {
        viewModelScope.launch {
            val current = dao.getByType(type) ?: return@launch
            val newProgress = current.progress + 1
            if (newProgress >= 10) {
                // Сбрасываем прогресс, НЕ увеличивая значение
                // Показываем диалог выбора улучшения
                saveChar(type, current.value, 0)
                _showUpgradeDialog.value = _showUpgradeDialog.value + (type to true)
            } else {
                saveChar(type, current.value, newProgress)
            }
        }
    }

    fun decrementProgress(type: CharacteristicType) {
        viewModelScope.launch {
            val current = dao.getByType(type) ?: return@launch
            if (current.progress > 0) {
                saveChar(type, current.value, current.progress - 1)
            }
        }
    }

    fun incrementSubValue(type: SubCharacteristicType) {
        viewModelScope.launch {
            val current = dao.getSubByType(type) ?: return@launch
            val parentType = subToParent[type] ?: return@launch
            val parent = dao.getByType(parentType) ?: return@launch
            // Подхарактеристика не может превышать характеристику более чем на 1
            if (current.value >= parent.value + 1) return@launch
            saveSub(type, current.value + 1, current.progress)
        }
    }

    fun decrementSubValue(type: SubCharacteristicType) {
        viewModelScope.launch {
            val current = dao.getSubByType(type) ?: return@launch
            if (current.value > 0) {
                saveSub(type, current.value - 1, current.progress)
            }
        }
    }

    fun incrementSubProgress(type: SubCharacteristicType) {
        viewModelScope.launch {
            val current = dao.getSubByType(type) ?: return@launch
            val newProgress = current.progress + 1
            if (newProgress >= 10) {
                saveSub(type, current.value + 1, 0)
            } else {
                saveSub(type, current.value, newProgress)
            }
        }
    }

    fun decrementSubProgress(type: SubCharacteristicType) {
        viewModelScope.launch {
            val current = dao.getSubByType(type) ?: return@launch
            if (current.progress > 0) {
                saveSub(type, current.value, current.progress - 1)
            }
        }
    }
}

data class CharacteristicData(val value: Int, val progress: Int)
data class SubCharacteristicData(val value: Int, val progress: Int)
