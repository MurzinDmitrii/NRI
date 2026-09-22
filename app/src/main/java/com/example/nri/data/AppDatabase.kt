package com.example.nri.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BagItem::class, Card::class, TabletCard::class, CharacterInfo::class, Characteristic::class, SubCharacteristic::class, Skill::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bagItemDao(): BagItemDao
    abstract fun cardDao(): CardDao
    abstract fun tabletCardDao(): TabletCardDao
    abstract fun characterInfoDao(): CharacterInfoDao
    abstract fun characteristicDao(): CharacteristicDao
    abstract fun skillDao(): SkillDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nri-database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = database.cardDao()
                                    DefaultCards.all.forEach { dao.insert(it) }
                                }
                            }
                        }
                    })
                    .build().also { INSTANCE = it }
            }
    }
}