package com.example.nri.ui.bag

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nri.data.BagItem
import com.example.nri.data.BagItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BagItemRow(
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
