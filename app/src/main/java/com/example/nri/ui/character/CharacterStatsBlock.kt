package com.example.nri.ui.character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nri.R
import com.example.nri.data.DiceType
import kotlinx.coroutines.flow.StateFlow

internal data class SubCharItem(
    val name: String,
    val dataFlow: StateFlow<SubCharacteristicData>,
    val diceFlow: StateFlow<DiceType?>,
    val onIncrementValue: () -> Unit,
    val onDecrementValue: () -> Unit,
    val onIncrementProgress: () -> Unit,
    val onDecrementProgress: () -> Unit,
    val descriptionRes: Int
)

/**
 * Цвета для типов кубов преимущества
 */
private val DiceType.color: Color
    @Composable
    get() = when (this) {
        DiceType.LIGHT_4 -> Color(0xFFFFA726) // оранжевый
        DiceType.MEDIUM_6 -> Color(0xFF66BB6A) // зелёный
        DiceType.STRONG_8 -> Color(0xFF42A5F5) // синий
        DiceType.MASTER_10 -> Color(0xFFAB47BC) // фиолетовый
        DiceType.PERFECT_12 -> Color(0xFFFF7043) // красно-оранжевый
        DiceType.DIVINE_20 -> Color(0xFFFF1744) // ярко-красный
    }

/**
 * Строка подхарактеристики — вертикальное расположение
 */
@Composable
private fun SubCharacteristicRow(sub: SubCharItem) {
    val data by sub.dataFlow.collectAsState()
    val dice by sub.diceFlow.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Название
            Text(
                text = sub.name,
                style = MaterialTheme.typography.titleMedium
            )

            // Описание
            Text(
                text = stringResource(sub.descriptionRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Куб, значение и кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Куб преимущества (только если есть преимущество)
                if (dice != null) {
                    val d = dice!!
                    Text(
                        text = d.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = d.color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }

                // Значение с кнопками
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = sub.onDecrementValue) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }
                    Text(
                        text = "${data.value}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = sub.onIncrementValue) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }
            }

            // Прогресс-бар подхарактеристики
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = sub.onDecrementProgress) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить прогресс")
                }
                LinearProgressIndicator(
                    progress = { data.progress / 10f },
                    modifier = Modifier.weight(1f).height(3.dp)
                )
                IconButton(onClick = sub.onIncrementProgress) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить прогресс")
                }
            }
            Text(
                text = "${data.progress} / 10",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

/**
 * Раскрывающаяся карточка характеристики — вертикальное расположение
 */
@Composable
internal fun CharacteristicCard(
    title: String,
    data: StateFlow<CharacteristicData>,
    dice: StateFlow<DiceType?>,
    description: String,
    color: Color,
    expanded: Boolean,
    onExpand: () -> Unit,
    onIncrementValue: () -> Unit,
    onDecrementValue: () -> Unit,
    onIncrementProgress: () -> Unit,
    onDecrementProgress: () -> Unit,
    subCharacteristics: List<SubCharItem>
) {
    val charData by data.collectAsState()
    val diceType by dice.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Заголовок с названием
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExpand)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = color
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть",
                    tint = color
                )
            }

            // Описание
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Куб, значение и кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Куб преимущества (только если есть преимущество)
                if (diceType != null) {
                    val d = diceType!!
                    Text(
                        text = d.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = d.color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(44.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(44.dp))
                }

                // Значение с кнопками
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrementValue) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }
                    Text(
                        text = "${charData.value}",
                        style = MaterialTheme.typography.titleLarge,
                        color = color,
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = onIncrementValue) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }
            }

            // Прогресс-бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrementProgress) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить прогресс")
                }
                LinearProgressIndicator(
                    progress = { charData.progress / 10f },
                    modifier = Modifier.weight(1f).height(4.dp),
                    color = color
                )
                IconButton(onClick = onIncrementProgress) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить прогресс")
                }
            }
            Text(
                text = "${charData.progress} / 10",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
            )

            // Подхарактеристики
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    subCharacteristics.forEach { sub ->
                        SubCharacteristicRow(sub)
                    }
                }
            }
        }
    }
}

/**
 * Блок характеристик с раскрывающимися карточками
 */
@Composable
internal fun CharacterStatsBlock(vm: CharacterStatsViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Характеристики",
                style = MaterialTheme.typography.titleLarge
            )

            var expandedStrength by remember { mutableStateOf(false) }
            var expandedAgility by remember { mutableStateOf(false) }
            var expandedIntelligence by remember { mutableStateOf(false) }

            CharacteristicCard(
                title = "Сила",
                data = vm.strength,
                dice = vm.strengthDice,
                description = stringResource(R.string.char_strength),
                color = MaterialTheme.colorScheme.error,
                expanded = expandedStrength,
                onExpand = { expandedStrength = !expandedStrength },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.STRENGTH) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.STRENGTH) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.STRENGTH) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.STRENGTH) },
                subCharacteristics = listOf(
                    SubCharItem("Выносливость", vm.endurance, vm.enduranceDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, com.example.nri.R.string.sub_endurance),
                    SubCharItem("Атлетизм", vm.athletics, vm.athleticsDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, com.example.nri.R.string.sub_athletics),
                    SubCharItem("Живучесть", vm.resilience, vm.resilienceDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, com.example.nri.R.string.sub_resilience)
                )
            )

            CharacteristicCard(
                title = "Ловкость",
                data = vm.agility,
                dice = vm.agilityDice,
                description = stringResource(R.string.char_agility),
                color = MaterialTheme.colorScheme.tertiary,
                expanded = expandedAgility,
                onExpand = { expandedAgility = !expandedAgility },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.AGILITY) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.AGILITY) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.AGILITY) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.AGILITY) },
                subCharacteristics = listOf(
                    SubCharItem("Скорость", vm.speed, vm.speedDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.SPEED) }, com.example.nri.R.string.sub_speed),
                    SubCharItem("Изворотливость", vm.evasion, vm.evasionDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.EVASION) }, com.example.nri.R.string.sub_evasion),
                    SubCharItem("Точность", vm.accuracy, vm.accuracyDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ACCURACY) }, com.example.nri.R.string.sub_accuracy)
                )
            )

            CharacteristicCard(
                title = "Интеллект",
                data = vm.intelligence,
                dice = vm.intelligenceDice,
                description = stringResource(R.string.char_intelligence),
                color = MaterialTheme.colorScheme.primary,
                expanded = expandedIntelligence,
                onExpand = { expandedIntelligence = !expandedIntelligence },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                subCharacteristics = listOf(
                    SubCharItem("Тактика", vm.tactics, vm.tacticsDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.TACTICS) }, com.example.nri.R.string.sub_tactics),
                    SubCharItem("Мудрость", vm.wisdom, vm.wisdomDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.WISDOM) }, com.example.nri.R.string.sub_wisdom),
                    SubCharItem("Восприятие", vm.perception, vm.perceptionDice, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, com.example.nri.R.string.sub_perception)
                )
            )
        }
    }
}
