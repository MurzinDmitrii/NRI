package com.example.nri.ui.character

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nri.R
import com.example.nri.data.BagItem

private data class InfoCard(
    val iconRes: Int,
    val label: String,
    val valueText: String
)

private val InfoCards = listOf(
    InfoCard(R.drawable.ic_shield, "Класс доспеха", "15"),
    InfoCard(R.drawable.ic_sword, "Урон", "1d8+3"),
    InfoCard(R.drawable.ic_coin, "Грамм", "250")
)

/**
 * Экран персонажа
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    vm: CharacterViewModel = viewModel(),
    statsVm: CharacterStatsViewModel = viewModel()
) {
    val grams by vm.grams.collectAsState()
    val armorClass by vm.armorClass.collectAsState()
    val armorList by vm.armorList.collectAsState()
    val selectedArmorName by vm.selectedArmorName.collectAsState()
    val health by vm.health.collectAsState()
    val maxHealth by statsVm.maxHealth.collectAsState()
    val weaponList by vm.weaponList.collectAsState()
    val selectedWeaponName by vm.selectedWeaponName.collectAsState()
    val weaponDamage by vm.weaponDamage.collectAsState()
    val athletics by statsVm.athletics.collectAsState()

    val totalDamage by remember(weaponDamage, athletics) {
        derivedStateOf {
            if (weaponDamage.isNotEmpty()) {
                "$weaponDamage + ${athletics.value}"
            } else {
                "—"
            }
        }
    }
    var gramsDialogOpen by remember { mutableStateOf(false) }
    var armorDialogOpen by remember { mutableStateOf(false) }
    var weaponDialogOpen by remember { mutableStateOf(false) }
    var healthDialogOpen by remember { mutableStateOf(false) }

    // Инициализация здоровья равным maxHealth, если не сохранено
    var initializedHealth by remember { mutableStateOf(false) }
    LaunchedEffect(maxHealth) {
        if (!initializedHealth) {
            vm.initHealthIfNotSet(maxHealth)
            initializedHealth = true
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Персонаж") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Блок информации
            CharacterInfoBlock(
                health = health,
                maxHealth = maxHealth,
                grams = grams,
                armorClass = armorClass,
                selectedArmorName = selectedArmorName,
                selectedWeaponName = selectedWeaponName,
                totalDamage = totalDamage,
                onHealthChange = { newHealth -> vm.saveHealth(newHealth) },
                onHealthDialogOpen = { healthDialogOpen = true },
                onGramsClick = { gramsDialogOpen = true },
                onArmorClick = { armorDialogOpen = true },
                onWeaponClick = { weaponDialogOpen = true }
            )

            // Блок характеристик
            CharacterStatsBlock(statsVm)
        }
    }

    if (gramsDialogOpen) {
        GramsDialog(
            initial = grams,
            onDismiss = { gramsDialogOpen = false },
            onConfirm = { value -> vm.saveGrams(value); gramsDialogOpen = false }
        )
    }

    if (armorDialogOpen) {
        ArmorDialog(
            armors = armorList,
            selectedName = selectedArmorName,
            onDismiss = { armorDialogOpen = false },
            onSelect = { armor ->
                vm.saveArmorClass(armor.armorClass.toString())
                vm.saveSelectedArmor(armor.name)
                armorDialogOpen = false
            },
            onClear = {
                vm.clearArmor()
                armorDialogOpen = false
            }
        )
    }

    if (weaponDialogOpen) {
        WeaponDialog(
            weapons = weaponList,
            selectedName = selectedWeaponName,
            onDismiss = { weaponDialogOpen = false },
            onSelect = { weapon ->
                vm.saveSelectedWeapon(weapon.name)
                vm.saveWeaponDamage(weapon.damage!!)
                weaponDialogOpen = false
            },
            onClear = {
                vm.clearWeapon()
                weaponDialogOpen = false
            }
        )
    }

    if (healthDialogOpen) {
        HealthDialog(
            initial = health,
            max = maxHealth,
            onDismiss = { healthDialogOpen = false },
            onConfirm = { value -> vm.saveHealth(value); healthDialogOpen = false }
        )
    }

}

/**
 * Блок информации персонажа: HP, КД, Урон, Грамм
 */

/**
 * Блок информации персонажа: HP, КД, Урон, Грамм
 */
@Composable
private fun CharacterInfoBlock(
    health: Int,
    maxHealth: Int,
    grams: String,
    armorClass: String,
    selectedArmorName: String,
    selectedWeaponName: String,
    totalDamage: String,
    onHealthChange: (Int) -> Unit,
    onHealthDialogOpen: () -> Unit,
    onGramsClick: () -> Unit,
    onArmorClick: () -> Unit,
    onWeaponClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Информация",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HealthCardItem(
                    current = health,
                    max = maxHealth,
                    onIncrement = { onHealthChange(health + 1) },
                    onDecrement = { onHealthChange(health - 1) },
                    onClick = onHealthDialogOpen,
                    modifier = Modifier.weight(1f)
                )
                InfoCardItem(
                    card = InfoCards[0].copy(valueText = armorClass),
                    modifier = Modifier.weight(1f),
                    onClick = onArmorClick
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeaponCardItem(
                    weaponName = selectedWeaponName,
                    damageText = totalDamage,
                    onClick = onWeaponClick,
                    modifier = Modifier.weight(1f)
                )
                InfoCardItem(
                    card = InfoCards[2].copy(valueText = grams),
                    modifier = Modifier.weight(1f),
                    onClick = onGramsClick
                )
            }
        }
    }
}

/**
 * Отдельная карточка с пиктограммой и значением
 */
@Composable
private fun InfoCardItem(
    card: InfoCard,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = card.iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = card.valueText,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * Карточка оружия с уроном
 */
@Composable
private fun WeaponCardItem(
    weaponName: String,
    damageText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.then(
            Modifier.clickable(onClick = onClick)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sword),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Урон",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (weaponName.isNotEmpty()) damageText else "—",
                    style = MaterialTheme.typography.titleMedium
                )
                if (weaponName.isNotEmpty()) {
                    Text(
                        text = weaponName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Карточка здоровья с кнопками +/-
 */
@Composable
private fun HealthCardItem(
    current: Int,
    max: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Здоровье",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$current / $max",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrement) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Уменьшить здоровье",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onIncrement) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Увеличить здоровье",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GramsDialog(
    initial: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(initial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Грамм") },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { input -> if (input.all { it.isDigit() }) value = input },
                label = { Text("Количество граммов") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArmorDialog(
    armors: List<BagItem>,
    selectedName: String,
    onDismiss: () -> Unit,
    onSelect: (BagItem) -> Unit,
    onClear: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите броню") },
        text = {
            if (armors.isEmpty()) {
                Text("В рюкзаке нет брони")
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    armors.forEachIndexed { index, armor ->
                        val isSelected = remember(index) { armor.name == selectedName }
                        ArmorListItem(
                            armor = armor,
                            isSelected = isSelected,
                            onClick = { onSelect(armor) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) {
                Text("Снять")
            }
        }
    )
}

@Composable
private fun ArmorListItem(
    armor: BagItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = armor.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "КД: ${armor.armorClass}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Выбрано",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeaponDialog(
    weapons: List<BagItem>,
    selectedName: String,
    onDismiss: () -> Unit,
    onSelect: (BagItem) -> Unit,
    onClear: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите оружие") },
        text = {
            if (weapons.isEmpty()) {
                Text("В рюкзаке нет оружия")
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    weapons.forEachIndexed { index, weapon ->
                        val isSelected = remember(index) { weapon.name == selectedName }
                        WeaponListItem(
                            weapon = weapon,
                            isSelected = isSelected,
                            onClick = { onSelect(weapon) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) {
                Text("Снять")
            }
        }
    )
}

@Composable
private fun WeaponListItem(
    weapon: BagItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = weapon.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = weapon.damage ?: "—",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Выбрано",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HealthDialog(
    initial: Int,
    max: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var value by remember { mutableStateOf(initial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Здоровье") },
        text = {
            Column {
                Text(
                    text = "Максимум: $max",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = value.toString(),
                    onValueChange = { input ->
                        val parsed = input.toIntOrNull()
                        if (parsed != null && parsed in 0..max) {
                            value = parsed
                        }
                    },
                    label = { Text("Текущее здоровье") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

/**
 * Блок характеристик с раскрывающимися карточками
 */
@Composable
private fun CharacterStatsBlock(vm: CharacterStatsViewModel) {
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
                description = stringResource(R.string.char_strength),
                color = MaterialTheme.colorScheme.error,
                expanded = expandedStrength,
                onExpand = { expandedStrength = !expandedStrength },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.STRENGTH) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.STRENGTH) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.STRENGTH) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.STRENGTH) },
                subCharacteristics = listOf(
                    SubCharItem("Выносливость", vm.endurance, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ENDURANCE) }, R.string.sub_endurance),
                    SubCharItem("Атлетизм", vm.athletics, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ATHLETICS) }, R.string.sub_athletics),
                    SubCharItem("Живучесть", vm.resilience, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.RESILIENCE) }, R.string.sub_resilience)
                )
            )

            CharacteristicCard(
                title = "Ловкость",
                data = vm.agility,
                description = stringResource(R.string.char_agility),
                color = MaterialTheme.colorScheme.tertiary,
                expanded = expandedAgility,
                onExpand = { expandedAgility = !expandedAgility },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.AGILITY) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.AGILITY) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.AGILITY) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.AGILITY) },
                subCharacteristics = listOf(
                    SubCharItem("Скорость", vm.speed, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.SPEED) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.SPEED) }, R.string.sub_speed),
                    SubCharItem("Изворотливость", vm.evasion, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.EVASION) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.EVASION) }, R.string.sub_evasion),
                    SubCharItem("Точность", vm.accuracy, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.ACCURACY) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.ACCURACY) }, R.string.sub_accuracy)
                )
            )

            CharacteristicCard(
                title = "Интеллект",
                data = vm.intelligence,
                description = stringResource(R.string.char_intelligence),
                color = MaterialTheme.colorScheme.primary,
                expanded = expandedIntelligence,
                onExpand = { expandedIntelligence = !expandedIntelligence },
                onIncrementValue = { vm.incrementValue(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onDecrementValue = { vm.decrementValue(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onIncrementProgress = { vm.incrementProgress(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                onDecrementProgress = { vm.decrementProgress(com.example.nri.data.CharacteristicType.INTELLIGENCE) },
                subCharacteristics = listOf(
                    SubCharItem("Тактика", vm.tactics, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.TACTICS) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.TACTICS) }, R.string.sub_tactics),
                    SubCharItem("Мудрость", vm.wisdom, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.WISDOM) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.WISDOM) }, R.string.sub_wisdom),
                    SubCharItem("Восприятие", vm.perception, { vm.incrementSubValue(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.decrementSubValue(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.incrementSubProgress(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, { vm.decrementSubProgress(com.example.nri.data.SubCharacteristicType.PERCEPTION) }, R.string.sub_perception)
                )
            )
        }
    }
}

private data class SubCharItem(
    val name: String,
    val dataFlow: StateFlow<SubCharacteristicData>,
    val onIncrementValue: () -> Unit,
    val onDecrementValue: () -> Unit,
    val onIncrementProgress: () -> Unit,
    val onDecrementProgress: () -> Unit,
    val descriptionRes: Int
)

/**
 * Строка подхарактеристики
 */
@Composable
private fun SubCharacteristicRow(sub: SubCharItem) {
    val data by sub.dataFlow.collectAsState()

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sub.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(sub.descriptionRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = sub.onDecrementValue) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }
                    Text(
                        text = "${data.value}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(24.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                    progress = data.progress / 10f,
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
 * Раскрывающаяся карточка характеристики
 */
@Composable
private fun CharacteristicCard(
    title: String,
    data: StateFlow<CharacteristicData>,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    expanded: Boolean,
    onExpand: () -> Unit,
    onIncrementValue: () -> Unit,
    onDecrementValue: () -> Unit,
    onIncrementProgress: () -> Unit,
    onDecrementProgress: () -> Unit,
    subCharacteristics: List<SubCharItem>
) {
    val charData by data.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Заголовок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExpand)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = color
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrementValue) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }
                    Text(
                        text = "${charData.value}",
                        style = MaterialTheme.typography.titleLarge,
                        color = color,
                        modifier = Modifier.width(32.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    IconButton(onClick = onIncrementValue) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Свернуть" else "Развернуть",
                    tint = color
                )
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
                    progress = charData.progress / 10f,
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
 * Блок информации персонажа: HP, КД, Урон, Грамм
 */
