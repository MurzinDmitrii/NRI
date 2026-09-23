package com.example.nri.ui.character

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nri.data.BagItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    vm: CharacterViewModel = viewModel(),
    statsVm: CharacterStatsViewModel = viewModel(),
    skillsVm: SkillsViewModel = viewModel()
) {
    val grams by vm.grams.collectAsState()
    val armorClass by vm.armorClass.collectAsState()
    val armorList by vm.armorList.collectAsState()
    val selectedArmorName by vm.selectedArmorName.collectAsState()
    val health by vm.health.collectAsState()
    val maxHealth by statsVm.maxHealth.collectAsState()
    val weaponList by vm.weaponList.collectAsState()
    val selectedWeaponName by vm.selectedWeaponName.collectAsState()
    val weaponDamage by vm.weaponDamage.collectAsState()
    val characterName by vm.characterName.collectAsState()
    val athletics by statsVm.athletics.collectAsState()

    val totalDamage by remember(weaponDamage, athletics) {
        derivedStateOf {
            if (weaponDamage.isNotEmpty()) {
                "$weaponDamage + ${athletics.value}"
            } else {
                "—"
            }
        }
    }
    var gramsDialogOpen by remember { mutableStateOf(false) }
    var armorDialogOpen by remember { mutableStateOf(false) }
    var weaponDialogOpen by remember { mutableStateOf(false) }
    var healthDialogOpen by remember { mutableStateOf(false) }
    var nameDialogOpen by remember { mutableStateOf(false) }

    // Инициализация здоровья равным maxHealth, если не сохранено
    var initializedHealth by remember { mutableStateOf(false) }
    LaunchedEffect(maxHealth) {
        if (!initializedHealth) {
            vm.initHealthIfNotSet(maxHealth)
            initializedHealth = true
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (characterName.isNotBlank()) characterName else "Персонаж",
                        modifier = Modifier.clickable { nameDialogOpen = true }
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Блок информации
            CharacterInfoBlock(
                health = health,
                maxHealth = maxHealth,
                grams = grams,
                armorClass = armorClass,
                selectedArmorName = selectedArmorName,
                selectedWeaponName = selectedWeaponName,
                totalDamage = totalDamage,
                onHealthChange = { newHealth -> vm.saveHealth(newHealth) },
                onHealthDialogOpen = { healthDialogOpen = true },
                onGramsClick = { gramsDialogOpen = true },
                onArmorClick = { armorDialogOpen = true },
                onWeaponClick = { weaponDialogOpen = true }
            )

            // Блок характеристик
            CharacterStatsBlock(statsVm)

            // Блок навыков
            SkillsBlock(skillsVm)
        }
    }

    if (gramsDialogOpen) {
        GramsDialog(
            initial = grams,
            onDismiss = { gramsDialogOpen = false },
            onConfirm = { value -> vm.saveGrams(value); gramsDialogOpen = false }
        )
    }

    if (armorDialogOpen) {
        ArmorDialog(
            armors = armorList,
            selectedName = selectedArmorName,
            onDismiss = { armorDialogOpen = false },
            onSelect = { armor ->
                vm.saveArmorClass(armor.armorClass.toString())
                vm.saveSelectedArmor(armor.name)
                armorDialogOpen = false
            },
            onClear = {
                vm.clearArmor()
                armorDialogOpen = false
            }
        )
    }

    if (weaponDialogOpen) {
        WeaponDialog(
            weapons = weaponList,
            selectedName = selectedWeaponName,
            onDismiss = { weaponDialogOpen = false },
            onSelect = { weapon ->
                vm.saveSelectedWeapon(weapon.name)
                vm.saveWeaponDamage(weapon.damage!!)
                weaponDialogOpen = false
            },
            onClear = {
                vm.clearWeapon()
                weaponDialogOpen = false
            }
        )
    }

    if (healthDialogOpen) {
        HealthDialog(
            initial = health,
            max = maxHealth,
            onDismiss = { healthDialogOpen = false },
            onConfirm = { value -> vm.saveHealth(value); healthDialogOpen = false }
        )
    }

    if (nameDialogOpen) {
        NameDialog(
            initial = characterName,
            onDismiss = { nameDialogOpen = false },
            onConfirm = { name ->
                vm.saveCharacterName(name)
                nameDialogOpen = false
            }
        )
    }
}
