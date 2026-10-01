package com.example.nri.data

/**
 * Типы кубов преимущества
 */
enum class DiceType(val label: String) {
    LIGHT_4("1d4"),
    MEDIUM_6("1d6"),
    STRONG_8("1d8"),
    MASTER_10("1d10"),
    PERFECT_12("1d12"),
    DIVINE_20("1d20");

    companion object {
        fun fromAdvantageLevel(level: Int): DiceType? {
            return when (level) {
                0 -> null // нет преимущества — нет куба
                1 -> LIGHT_4
                2 -> MEDIUM_6
                3 -> STRONG_8
                4 -> MASTER_10
                5 -> PERFECT_12
                else -> DIVINE_20
            }
        }
    }
}

/**
 * Вычисление уровня преимущества и типа куба для основной характеристики
 * (1 уровень преимущества за каждые 3 очка развития)
 */
fun calculateMainCharAdvantage(value: Int): Pair<Int, DiceType?> {
    val level = value / 3
    val dice = DiceType.fromAdvantageLevel(level)
    return level to dice
}

/**
 * Вычисление уровня преимущества и типа куба для подхарактеристики
 * 
 * Endurance, Perception: пороги 2, 4, 6, 8, 10 (уровень = value / 2)
 * Остальные: пороги 3, 6, 9, 12, 15 (уровень = value / 3)
 */
fun calculateSubCharAdvantage(type: SubCharacteristicType, value: Int): Pair<Int, DiceType?> {
    val divisor = when (type) {
        SubCharacteristicType.ENDURANCE, SubCharacteristicType.PERCEPTION -> 2
        else -> 3
    }
    val level = value / divisor
    val dice = DiceType.fromAdvantageLevel(level)
    return level to dice
}

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
