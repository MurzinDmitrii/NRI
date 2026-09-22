package com.example.nri.ui.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nri.data.AppDatabase
import com.example.nri.data.Skill
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SkillData(val id: Int, val name: String, val value: Int, val progress: Int)

class SkillsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).skillDao()

    val skills: StateFlow<List<SkillData>> = dao.getAll()
        .map { list -> list.map { SkillData(it.id, it.name, it.value, it.progress) } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addSkill(name: String) {
        viewModelScope.launch {
            val newSkill = Skill(id = 0, name = name, value = 0, progress = 0)
            dao.upsert(newSkill)
        }
    }

    fun incrementValue(skillId: Int) {
        viewModelScope.launch {
            val skill = dao.getById(skillId) ?: return@launch
            dao.upsert(skill.copy(value = skill.value + 1))
        }
    }

    fun decrementValue(skillId: Int) {
        viewModelScope.launch {
            val skill = dao.getById(skillId) ?: return@launch
            if (skill.value > 0) {
                dao.upsert(skill.copy(value = skill.value - 1))
            }
        }
    }

    fun incrementProgress(skillId: Int) {
        viewModelScope.launch {
            val skill = dao.getById(skillId) ?: return@launch
            val newProgress = skill.progress + 1
            if (newProgress >= 10) {
                dao.upsert(skill.copy(value = skill.value + 1, progress = 0))
            } else {
                dao.upsert(skill.copy(progress = newProgress))
            }
        }
    }

    fun decrementProgress(skillId: Int) {
        viewModelScope.launch {
            val skill = dao.getById(skillId) ?: return@launch
            if (skill.progress > 0) {
                dao.upsert(skill.copy(progress = skill.progress - 1))
            }
        }
    }
}
