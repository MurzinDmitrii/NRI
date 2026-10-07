package com.example.nri.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM settings WHERE key = :key LIMIT 1")
    fun getSetting(key: String): Flow<Setting?>

    @Query("SELECT * FROM settings WHERE key = :key LIMIT 1")
    suspend fun getSettingValue(key: String): Setting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(setting: Setting)

    @Query("UPDATE settings SET valueInt = :value WHERE key = :key")
    suspend fun updateIntValue(key: String, value: Int)
}
