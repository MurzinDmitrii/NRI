package com.example.nri.ui.tablet

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

@Composable
internal fun TabletCardRow(
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
