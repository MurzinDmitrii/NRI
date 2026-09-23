package com.example.nri.ui.notes

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
import com.example.nri.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    vm: NoteViewModel = viewModel()
) {
    val notes by vm.notes.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingNote = null
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет заметок")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onEdit = {
                            editingNote = note
                            showEditDialog = true
                        },
                        onDelete = { vm.delete(note) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        NoteDialog(
            title = "Новая заметка",
            initialTitle = "",
            initialContent = "",
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content ->
                vm.save(Note(title = title, content = content))
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && editingNote != null) {
        NoteDialog(
            title = "Редактировать",
            initialTitle = editingNote!!.title,
            initialContent = editingNote!!.content,
            onDismiss = { showEditDialog = false },
            onConfirm = { newTitle, newContent ->
                vm.save(editingNote!!.copy(title = newTitle, content = newContent))
                showEditDialog = false
            }
        )
    }
}
