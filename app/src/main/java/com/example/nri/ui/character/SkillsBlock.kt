package com.example.nri.ui.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Блок навыков с возможностью добавления
 */
@Composable
internal fun SkillsBlock(vm: SkillsViewModel) {
    val skills by vm.skills.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editSkill by remember { mutableStateOf<SkillData?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Заголовок с кнопкой добавления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Навыки",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить навык"
                    )
                }
            }

            if (skills.isEmpty()) {
                Text(
                    text = "Нет навыков. Нажмите +, чтобы добавить.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skills.forEach { skill ->
                        SkillCard(
                            skill = skill,
                            onIncrementValue = { vm.incrementValue(skill.id) },
                            onDecrementValue = { vm.decrementValue(skill.id) },
                            onIncrementProgress = { vm.incrementProgress(skill.id) },
                            onDecrementProgress = { vm.decrementProgress(skill.id) },
                            onEditClick = { editSkill = skill }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddSkillDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                vm.addSkill(name)
                showAddDialog = false
            }
        )
    }

    editSkill?.let { skill ->
        EditSkillDialog(
            skill = skill,
            onDismiss = { editSkill = null },
            onNameChange = { newName ->
                vm.updateSkillName(skill.id, newName)
                editSkill = null
            },
            onDelete = {
                vm.deleteSkill(skill.id)
                editSkill = null
            }
        )
    }
}

/**
 * Карточка навыка
 */
@Composable
private fun SkillCard(
    skill: SkillData,
    onIncrementValue: () -> Unit,
    onDecrementValue: () -> Unit,
    onIncrementProgress: () -> Unit,
    onDecrementProgress: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Заголовок с названием
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = skill.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrementValue) {
                        Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
                    }
                    Text(
                        text = "${skill.value}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.width(32.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = onIncrementValue) {
                        Icon(Icons.Default.Add, contentDescription = "Увеличить")
                    }
                }
            }

            // Прогресс-бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrementProgress) {
                    Icon(Icons.Default.Remove, contentDescription = "Уменьшить прогресс")
                }
                LinearProgressIndicator(
                    progress = skill.progress / 10f,
                    modifier = Modifier.weight(1f).height(4.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                IconButton(onClick = onIncrementProgress) {
                    Icon(Icons.Default.Add, contentDescription = "Увеличить прогресс")
                }
            }
            Text(
                text = "${skill.progress} / 10",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
            )
        }
    }
}

/**
 * Диалог добавления нового навыка
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSkillDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый навык") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название навыка") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Например: Боевое мастерство") }
            )
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name.trim()) }
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

/**
 * Диалог редактирования навыка
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSkillDialog(
    skill: SkillData,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(skill.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать навык") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название навыка") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Column {
                TextButton(
                    enabled = name.isNotBlank(),
                    onClick = { onNameChange(name.trim()) }
                ) {
                    Text("Сохранить")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onDelete) {
                        Text(
                            "Удалить",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
