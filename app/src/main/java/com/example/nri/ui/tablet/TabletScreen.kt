package com.example.nri.ui.tablet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nri.data.BagItem
import com.example.nri.data.TabletCardWithBagItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletScreen(
    modifier: Modifier = Modifier,
    vm: TabletViewModel = viewModel()
) {
    val tabletCards by vm.tabletCards.collectAsState()
    val maxCards by vm.maxCards.collectAsState()
    val availableCardItems by vm.availableCardItems.collectAsState()
    var maxCardsInput by remember { mutableStateOf(maxCards.toString()) }

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBagItemId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (tabletCards.size < maxCards) {
                FloatingActionButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить карту")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Поле ввода максимального количества карт
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Макс. карт:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.widthIn(min = 100.dp)
                    )
                    OutlinedTextField(
                        value = maxCardsInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                maxCardsInput = input
                                vm.setMaxCards(input.toIntOrNull() ?: 1)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(80.dp)
                    )
                    Text(
                        text = "(${tabletCards.size}/$maxCards)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Список карт на планшете
            if (tabletCards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет выбранных карт. Добавьте карту из инвентаря.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tabletCards, key = { it.tabletCard.id }) { item ->
                        TabletCardRow(
                            bagItem = item.bagItem,
                            onRemove = { vm.removeCard(item.tabletCard.id) },
                            onUsesChange = { delta -> vm.changeUses(item.bagItem, delta) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCardDialog(
            availableCards = availableCardItems,
            existingBagItemIds = tabletCards.map { it.tabletCard.bagItemId },
            maxSlots = maxCards - tabletCards.size,
            selectedBagItemId = selectedBagItemId,
            onSelected = { id -> selectedBagItemId = id },
            onDismiss = { showAddDialog = false; selectedBagItemId = null },
            onConfirm = { bagItemId ->
                vm.addCard(bagItemId)
                showAddDialog = false
                selectedBagItemId = null
            }
        )
    }
}

@Composable
private fun TabletCardRow(
    bagItem: BagItem,
    onRemove: () -> Unit,
    onUsesChange: (Int) -> Unit
) {
    val uses = bagItem.uses ?: 0

    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(bagItem.name, style = MaterialTheme.typography.titleMedium)
                if (bagItem.description.isNotBlank()) {
                    Text(bagItem.description, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = if (uses > 0) "Использований: $uses" else "Использований: ∞",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { if (uses > 0) onUsesChange(-1) },
                    enabled = uses > 0
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                }
                Text(
                    text = uses.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { onUsesChange(1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCardDialog(
    availableCards: List<BagItem>,
    existingBagItemIds: List<Long>,
    maxSlots: Int,
    selectedBagItemId: Long?,
    onSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (bagItemId: Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredCards = availableCards.filter { it.id !in existingBagItemIds }
    val selectedItem = selectedBagItemId?.let { id -> availableCards.find { it.id == id } }
    val selectedUsesText = selectedItem?.uses?.let { " - $it использований" } ?: " - ∞ использований"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить карту на планшет") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Доступно слотов: $maxSlots",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (filteredCards.isEmpty()) {
                    Text("Все карты из инвентаря уже добавлены на планшет.")
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedItem?.let { "${it.name}${selectedUsesText}" } ?: "Выберите карту",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Карта из инвентаря") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredCards.forEach { card ->
                                val usesText = card.uses?.let { " - $it использований" } ?: " - ∞ использований"
                                DropdownMenuItem(
                                    text = { Text("${card.name}$usesText") },
                                    onClick = {
                                        onSelected(card.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedBagItemId?.let { id ->
                        onConfirm(id)
                    }
                },
                enabled = selectedBagItemId != null
            ) { Text("Добавить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}
