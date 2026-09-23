package com.example.nri.ui.tablet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
