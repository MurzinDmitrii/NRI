package com.example.nri.ui.tablet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nri.data.BagItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddCardDialog(
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
