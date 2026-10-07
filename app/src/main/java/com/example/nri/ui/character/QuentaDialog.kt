package com.example.nri.ui.character

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuentaDialog(
    initialName: String,
    initialBiography: String,
    initialAlignment: String,
    initialInterestingFeatures: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, biography: String, alignment: String, interestingFeatures: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var biography by remember { mutableStateOf(initialBiography) }
    var alignment by remember { mutableStateOf(initialAlignment) }
    var interestingFeatures by remember { mutableStateOf(initialInterestingFeatures) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Квента персонажа") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = biography,
                    onValueChange = { biography = it },
                    label = { Text("Биография") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = alignment,
                    onValueChange = { alignment = it },
                    label = { Text("Мировоззрение") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = interestingFeatures,
                    onValueChange = { interestingFeatures = it },
                    label = { Text("Интересные особенности") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = {
                    onConfirm(
                        name.trim(),
                        biography.trim(),
                        alignment.trim(),
                        interestingFeatures.trim()
                    )
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
