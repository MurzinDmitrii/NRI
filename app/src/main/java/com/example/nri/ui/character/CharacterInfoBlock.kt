package com.example.nri.ui.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nri.R

private data class InfoCard(
    val iconRes: Int,
    val label: String,
    val valueText: String
)

private val InfoCards = listOf(
    InfoCard(R.drawable.ic_shield, "Класс доспеха", "15"),
    InfoCard(R.drawable.ic_sword, "Урон", "1d8+3"),
    InfoCard(R.drawable.ic_coin, "Грамм", "250")
)

@Composable
internal fun CharacterInfoBlock(
    health: Int,
    maxHealth: Int,
    grams: String,
    armorClass: String,
    selectedArmorName: String,
    selectedWeaponName: String,
    totalDamage: String,
    onHealthChange: (Int) -> Unit,
    onHealthDialogOpen: () -> Unit,
    onGramsClick: () -> Unit,
    onArmorClick: () -> Unit,
    onWeaponClick: () -> Unit,
    totalActions: String,
    bonusActions: String,
    movementDistance: String,
    speedAdvantageLevel: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Информация",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HealthCardItem(
                    current = health,
                    max = maxHealth,
                    onIncrement = { onHealthChange(health + 1) },
                    onDecrement = { onHealthChange(health - 1) },
                    onClick = onHealthDialogOpen,
                    modifier = Modifier.weight(1f)
                )
                InfoCardItem(
                    card = InfoCards[0].copy(valueText = armorClass),
                    modifier = Modifier.weight(1f),
                    onClick = onArmorClick
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeaponCardItem(
                    weaponName = selectedWeaponName,
                    damageText = totalDamage,
                    onClick = onWeaponClick,
                    modifier = Modifier.weight(1f)
                )
                InfoCardItem(
                    card = InfoCards[2].copy(valueText = grams),
                    modifier = Modifier.weight(1f),
                    onClick = onGramsClick
                )
            }

            // Блок действий и скорости в одной строке
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionsCardItem(
                    totalActions = totalActions,
                    bonusActions = bonusActions,
                    modifier = Modifier.weight(1f)
                )
                SpeedCardItem(
                    movementDistance = movementDistance,
                    advantageLevel = speedAdvantageLevel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InfoCardItem(
    card: InfoCard,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = card.iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = card.valueText,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun WeaponCardItem(
    weaponName: String,
    damageText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.then(
            Modifier.clickable(onClick = onClick)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sword),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Урон",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (weaponName.isNotEmpty()) damageText else "—",
                    style = MaterialTheme.typography.titleMedium
                )
                if (weaponName.isNotEmpty()) {
                    Text(
                        text = weaponName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthCardItem(
    current: Int,
    max: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Здоровье",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$current / $max",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrement) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Уменьшить здоровье",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onIncrement) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Увеличить здоровье",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionsCardItem(
    totalActions: String,
    bonusActions: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_action),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Действия",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$totalActions действие",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Доп. действия: $bonusActions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SpeedCardItem(
    movementDistance: String,
    advantageLevel: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_speed),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Скорость",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Скорость: $movementDistance",
                    style = MaterialTheme.typography.titleMedium
                )
                if (advantageLevel > 0) {
                    Text(
                        text = "Преимущество: $advantageLevel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
