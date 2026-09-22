package com.example.nri.data

/**
 * Основные характеристики персонажа
 */
enum class CharacteristicType {
    STRENGTH,
    AGILITY,
    INTELLIGENCE
}

/**
 * Подхарактеристики (навыки прокачки)
 */
enum class SubCharacteristicType {
    // Сила
    ENDURANCE,
    ATHLETICS,
    RESILIENCE,
    // Ловкость
    SPEED,
    EVASION,
    ACCURACY,
    // Интеллект
    TACTICS,
    WISDOM,
    PERCEPTION
}
