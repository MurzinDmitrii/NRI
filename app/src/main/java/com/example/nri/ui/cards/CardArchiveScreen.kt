package com.example.nri.ui.cards

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
import com.example.nri.data.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardArchiveScreen(
    modifier: Modifier = Modifier,
    vm: CardArchiveViewModel = viewModel()
) {
    val cards by vm.cards.collectAsState()
    var editing by remember { mutableStateOf<Card?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editing = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить карту")
            }
        }
    ) { padding ->
        if (cards.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Архив карт пуст")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards, key = { it.id }) { card ->
                    CardRow(
                        card = card,
                        onEdit = { editing = card; showDialog = true },
                        onDelete = { vm.delete(card) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        CardDialog(
            initial = editing,
            onDismiss = { showDialog = false },
            onConfirm = { card ->
                vm.save(card)
                showDialog = false
            }
        )
    }
}
