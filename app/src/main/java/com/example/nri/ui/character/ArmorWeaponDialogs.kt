package com.example.nri.ui.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nri.data.BagItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArmorDialog(
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
internal fun WeaponDialog(
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
