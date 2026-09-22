package com.example.nri.ui.bag

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
import com.example.nri.data.BagItemType
import com.example.nri.data.Card
import com.example.nri.data.WeaponType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(
    modifier: Modifier = Modifier,
    vm: BagViewModel = viewModel()
) {
    val items by vm.items.collectAsState()
    val cards by vm.cards.collectAsState()
    var editing by remember { mutableStateOf<BagItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editing = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Рюкзак пуст")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    BagItemRow(
                        item = item,
                        onEdit = { editing = item; showDialog = true },
                        onDelete = { vm.delete(item) },
                        onQuantityChange = { delta -> vm.changeQuantity(item, delta) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        BagItemDialog(
            initial = editing,
            cards = cards,
            onDismiss = { showDialog = false },
            onConfirm = { item ->
                vm.save(item)
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BagItemRow(
    item: BagItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        onClick = onEdit,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(item.type.displayName, style = MaterialTheme.typography.labelMedium)

                if (item.type == BagItemType.WEAPON) {
                    item.weaponType?.let {
                        Text("Тип оружия: ${it.displayName}", style = MaterialTheme.typography.bodySmall)
                    }
                    item.damage?.let {
                        Text("Урон: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (item.type == BagItemType.ARMOR) {
                    item.armorClass?.let {
                        Text("КД: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (item.type == BagItemType.CARD) {
                    item.uses?.let {
                        Text("Использований: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (item.description.isNotBlank()) {
                    Text(item.description, style = MaterialTheme.typography.bodySmall)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQuantityChange(-1) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                }
                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { onQuantityChange(+1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить")
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BagItemDialog(
    initial: BagItem?,
    cards: List<Card>,
    onDismiss: () -> Unit,
    onConfirm: (BagItem) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var type by remember { mutableStateOf(initial?.type ?: BagItemType.MISC) }
    var quantity by remember { mutableStateOf((initial?.quantity ?: 1).toString()) }

    var weaponType by remember { mutableStateOf(initial?.weaponType ?: WeaponType.ONE_HANDED) }
    var damage by remember { mutableStateOf(initial?.damage ?: "") }

    var armorClass by remember { mutableStateOf(initial?.armorClass?.toString() ?: "") }

    var typeMenuOpen by remember { mutableStateOf(false) }
    var weaponMenuOpen by remember { mutableStateOf(false) }
    var cardMenuOpen by remember { mutableStateOf(false) }

    var uses by remember { mutableStateOf(initial?.uses?.toString() ?: "10") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Новая вещь" else "Редактировать") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Тип — первое поле
                ExposedDropdownMenuBox(
                    expanded = typeMenuOpen,
                    onExpandedChange = { typeMenuOpen = !typeMenuOpen }
                ) {
                    OutlinedTextField(
                        value = type.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Тип") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeMenuOpen) },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeMenuOpen,
                        onDismissRequest = { typeMenuOpen = false }
                    ) {
                        BagItemType.entries.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.displayName) },
                                onClick = { type = t; typeMenuOpen = false }
                            )
                        }
                    }
                }

                // Название: для CARD — выпадающий список карт, для остальных — текстовое поле
                if (type == BagItemType.CARD) {
                    ExposedDropdownMenuBox(
                        expanded = cardMenuOpen,
                        onExpandedChange = { cardMenuOpen = !cardMenuOpen }
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Карта") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cardMenuOpen) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = cardMenuOpen,
                            onDismissRequest = { cardMenuOpen = false }
                        ) {
                            if (cards.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Сначала добавьте карты в Архив") },
                                    onClick = { cardMenuOpen = false },
                                    enabled = false
                                )
                            } else {
                                cards.forEach { card ->
                                    DropdownMenuItem(
                                        text = { Text(card.name) },
                                        onClick = {
                                            name = card.name
                                            description = card.description
                                            cardMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название") },
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    readOnly = type == BagItemType.CARD
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { input -> if (input.all { it.isDigit() }) quantity = input },
                    label = { Text("Количество") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (type == BagItemType.CARD) {
                    OutlinedTextField(
                        value = uses,
                        onValueChange = { input -> if (input.all { it.isDigit() }) uses = input },
                        label = { Text("Количество использований") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (type == BagItemType.WEAPON) {
                    ExposedDropdownMenuBox(
                        expanded = weaponMenuOpen,
                        onExpandedChange = { weaponMenuOpen = !weaponMenuOpen }
                    ) {
                        OutlinedTextField(
                            value = weaponType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Тип оружия") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(weaponMenuOpen) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = weaponMenuOpen,
                            onDismissRequest = { weaponMenuOpen = false }
                        ) {
                            WeaponType.entries.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text(w.displayName) },
                                    onClick = { weaponType = w; weaponMenuOpen = false }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = damage,
                        onValueChange = { damage = it },
                        label = { Text("Урон (например, 1d6+2)") },
                        singleLine = true
                    )
                }

                if (type == BagItemType.ARMOR) {
                    OutlinedTextField(
                        value = armorClass,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) armorClass = input
                        },
                        label = { Text("КД (класс доспеха)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val result = BagItem(
                            id = initial?.id ?: 0,
                            name = name.trim(),
                            description = description.trim(),
                            type = type,
                            quantity = quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1,
                            weaponType = if (type == BagItemType.WEAPON) weaponType else null,
                            damage = if (type == BagItemType.WEAPON) damage.trim().ifBlank { null } else null,
                            armorClass = if (type == BagItemType.ARMOR) armorClass.toIntOrNull() else null,
                            uses = if (type == BagItemType.CARD) uses.toIntOrNull() else null,
                        )
                        onConfirm(result)
                    }
                }
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}