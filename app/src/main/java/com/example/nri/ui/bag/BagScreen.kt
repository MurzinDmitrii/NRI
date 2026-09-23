package com.example.nri.ui.bag

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
import com.example.nri.data.BagItem

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
