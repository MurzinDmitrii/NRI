package com.example.nri.ui.character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.nri.data.CharacteristicType
import com.example.nri.data.SubCharacteristicType
import com.example.nri.R
import com.example.nri.data.DiceType
import kotlinx.coroutines.flow.StateFlow

internal data class SubCharItem(
    val name: String,
    val type: com.example.nri.data.SubCharacteristicType,
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
    val showUpgradeDialog by vm.showUpgradeDialog.collectAsState()

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
                onIncrementValue = { vm.incrementValue(CharacteristicType.STRENGTH) },
                onDecrementValue = { vm.decrementValue(CharacteristicType.STRENGTH) },
                onIncrementProgress = { vm.incrementProgress(CharacteristicType.STRENGTH) },
                onDecrementProgress = { vm.decrementProgress(CharacteristicType.STRENGTH) },
                subCharacteristics = listOf(
                    SubCharItem("Выносливость", SubCharacteristicType.ENDURANCE, vm.endurance, vm.enduranceDice, { vm.incrementSubValue(SubCharacteristicType.ENDURANCE) }, { vm.decrementSubValue(SubCharacteristicType.ENDURANCE) }, { vm.incrementSubProgress(SubCharacteristicType.ENDURANCE) }, { vm.decrementSubProgress(SubCharacteristicType.ENDURANCE) }, R.string.sub_endurance),
                    SubCharItem("Атлетизм", SubCharacteristicType.ATHLETICS, vm.athletics, vm.athleticsDice, { vm.incrementSubValue(SubCharacteristicType.ATHLETICS) }, { vm.decrementSubValue(SubCharacteristicType.ATHLETICS) }, { vm.incrementSubProgress(SubCharacteristicType.ATHLETICS) }, { vm.decrementSubProgress(SubCharacteristicType.ATHLETICS) }, R.string.sub_athletics),
                    SubCharItem("Живучесть", SubCharacteristicType.RESILIENCE, vm.resilience, vm.resilienceDice, { vm.incrementSubValue(SubCharacteristicType.RESILIENCE) }, { vm.decrementSubValue(SubCharacteristicType.RESILIENCE) }, { vm.incrementSubProgress(SubCharacteristicType.RESILIENCE) }, { vm.decrementSubProgress(SubCharacteristicType.RESILIENCE) }, R.string.sub_resilience)
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
                onIncrementValue = { vm.incrementValue(CharacteristicType.AGILITY) },
                onDecrementValue = { vm.decrementValue(CharacteristicType.AGILITY) },
                onIncrementProgress = { vm.incrementProgress(CharacteristicType.AGILITY) },
                onDecrementProgress = { vm.decrementProgress(CharacteristicType.AGILITY) },
                subCharacteristics = listOf(
                    SubCharItem("Скорость", SubCharacteristicType.SPEED, vm.speed, vm.speedDice, { vm.incrementSubValue(SubCharacteristicType.SPEED) }, { vm.decrementSubValue(SubCharacteristicType.SPEED) }, { vm.incrementSubProgress(SubCharacteristicType.SPEED) }, { vm.decrementSubProgress(SubCharacteristicType.SPEED) }, R.string.sub_speed),
                    SubCharItem("Изворотливость", SubCharacteristicType.EVASION, vm.evasion, vm.evasionDice, { vm.incrementSubValue(SubCharacteristicType.EVASION) }, { vm.decrementSubValue(SubCharacteristicType.EVASION) }, { vm.incrementSubProgress(SubCharacteristicType.EVASION) }, { vm.decrementSubProgress(SubCharacteristicType.EVASION) }, R.string.sub_evasion),
                    SubCharItem("Точность", SubCharacteristicType.ACCURACY, vm.accuracy, vm.accuracyDice, { vm.incrementSubValue(SubCharacteristicType.ACCURACY) }, { vm.decrementSubValue(SubCharacteristicType.ACCURACY) }, { vm.incrementSubProgress(SubCharacteristicType.ACCURACY) }, { vm.decrementSubProgress(SubCharacteristicType.ACCURACY) }, R.string.sub_accuracy)
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
                onIncrementValue = { vm.incrementValue(CharacteristicType.INTELLIGENCE) },
                onDecrementValue = { vm.decrementValue(CharacteristicType.INTELLIGENCE) },
                onIncrementProgress = { vm.incrementProgress(CharacteristicType.INTELLIGENCE) },
                onDecrementProgress = { vm.decrementProgress(CharacteristicType.INTELLIGENCE) },
                subCharacteristics = listOf(
                    SubCharItem("Тактика", SubCharacteristicType.TACTICS, vm.tactics, vm.tacticsDice, { vm.incrementSubValue(SubCharacteristicType.TACTICS) }, { vm.decrementSubValue(SubCharacteristicType.TACTICS) }, { vm.incrementSubProgress(SubCharacteristicType.TACTICS) }, { vm.decrementSubProgress(SubCharacteristicType.TACTICS) }, R.string.sub_tactics),
                    SubCharItem("Мудрость", SubCharacteristicType.WISDOM, vm.wisdom, vm.wisdomDice, { vm.incrementSubValue(SubCharacteristicType.WISDOM) }, { vm.decrementSubValue(SubCharacteristicType.WISDOM) }, { vm.incrementSubProgress(SubCharacteristicType.WISDOM) }, { vm.decrementSubProgress(SubCharacteristicType.WISDOM) }, R.string.sub_wisdom),
                    SubCharItem("Восприятие", SubCharacteristicType.PERCEPTION, vm.perception, vm.perceptionDice, { vm.incrementSubValue(SubCharacteristicType.PERCEPTION) }, { vm.decrementSubValue(SubCharacteristicType.PERCEPTION) }, { vm.incrementSubProgress(SubCharacteristicType.PERCEPTION) }, { vm.decrementSubProgress(SubCharacteristicType.PERCEPTION) }, R.string.sub_perception)
                )
            )
        }
    }

    // Диалог выбора улучшения для Силы
    if (showUpgradeDialog[CharacteristicType.STRENGTH] == true) {
        UpgradeChoiceDialog(
            title = "Сила",
            subCharacteristics = listOf(
                "Выносливость" to SubCharacteristicType.ENDURANCE,
                "Атлетизм" to SubCharacteristicType.ATHLETICS,
                "Живучесть" to SubCharacteristicType.RESILIENCE
            ),
            onUpgradeMain = {
                vm.incrementValue(CharacteristicType.STRENGTH)
                vm.clearUpgradeDialog(CharacteristicType.STRENGTH)
            },
            onUpgradeSub = { subType ->
                vm.incrementSubValue(subType)
                vm.clearUpgradeDialog(CharacteristicType.STRENGTH)
            },
            onDismiss = { vm.clearUpgradeDialog(CharacteristicType.STRENGTH) }
        )
    }

    // Диалог выбора улучшения для Ловкости
    if (showUpgradeDialog[CharacteristicType.AGILITY] == true) {
        UpgradeChoiceDialog(
            title = "Ловкость",
            subCharacteristics = listOf(
                "Скорость" to SubCharacteristicType.SPEED,
                "Изворотливость" to SubCharacteristicType.EVASION,
                "Точность" to SubCharacteristicType.ACCURACY
            ),
            onUpgradeMain = {
                vm.incrementValue(CharacteristicType.AGILITY)
                vm.clearUpgradeDialog(CharacteristicType.AGILITY)
            },
            onUpgradeSub = { subType ->
                vm.incrementSubValue(subType)
                vm.clearUpgradeDialog(CharacteristicType.AGILITY)
            },
            onDismiss = { vm.clearUpgradeDialog(CharacteristicType.AGILITY) }
        )
    }

    // Диалог выбора улучшения для Интеллекта
    if (showUpgradeDialog[CharacteristicType.INTELLIGENCE] == true) {
        UpgradeChoiceDialog(
            title = "Интеллект",
            subCharacteristics = listOf(
                "Тактика" to SubCharacteristicType.TACTICS,
                "Мудрость" to SubCharacteristicType.WISDOM,
                "Восприятие" to SubCharacteristicType.PERCEPTION
            ),
            onUpgradeMain = {
                vm.incrementValue(CharacteristicType.INTELLIGENCE)
                vm.clearUpgradeDialog(CharacteristicType.INTELLIGENCE)
            },
            onUpgradeSub = { subType ->
                vm.incrementSubValue(subType)
                vm.clearUpgradeDialog(CharacteristicType.INTELLIGENCE)
            },
            onDismiss = { vm.clearUpgradeDialog(CharacteristicType.INTELLIGENCE) }
        )
    }
}

/**
 * Диалог выбора улучшения при достижении прогрессом значения 10
 */
@Composable
private fun UpgradeChoiceDialog(
    title: String,
    subCharacteristics: List<Pair<String, SubCharacteristicType>>,
    onUpgradeMain: () -> Unit,
    onUpgradeSub: (SubCharacteristicType) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите улучшение") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$title достигла максимума прогресса. Что улучшить?",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Увеличить основную характеристику
                Button(
                    onClick = onUpgradeMain,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Увеличить $title на 1")
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Или выберите подхарактеристику:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Увеличить подхарактеристику
                subCharacteristics.forEach { (name, type) ->
                    Button(
                        onClick = { onUpgradeSub(type) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Увеличить $name на 1")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
