package com.example.nri.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Сущность для хранения навыков
 */
@Entity(tableName = "skills")
data class Skill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val value: Int,
    val progress: Int
)

/**
 * DAO для работы с навыками
 */
@Dao
interface SkillDao {
    @Query("SELECT * FROM skills ORDER BY id ASC")
    fun getAll(): Flow<List<Skill>>

    @Query("SELECT * FROM skills WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Skill?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: Skill)
}
