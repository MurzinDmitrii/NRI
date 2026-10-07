package com.example.nri.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BagItem::class, Card::class, TabletCard::class, BeltItem::class, Setting::class, CharacterInfo::class, Characteristic::class, SubCharacteristic::class, Skill::class, Note::class],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bagItemDao(): BagItemDao
    abstract fun cardDao(): CardDao
    abstract fun tabletCardDao(): TabletCardDao
    abstract fun beltItemDao(): BeltItemDao
    abstract fun settingDao(): SettingDao
    abstract fun characterInfoDao(): CharacterInfoDao
    abstract fun characteristicDao(): CharacteristicDao
    abstract fun skillDao(): SkillDao
    abstract fun noteDao(): NoteDao

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
                    .addMigrations(MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13)
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

private val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE bag_items ADD COLUMN damageType TEXT"
        )
    }
}

private val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE belt_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                bagItemId INTEGER NOT NULL,
                FOREIGN KEY(bagItemId) REFERENCES bag_items(id) ON DELETE CASCADE
            )"""
        )
        db.execSQL("CREATE INDEX `index_belt_items_bagItemId` ON `belt_items` (`bagItemId`)")
    }
}

private val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE settings (
                key TEXT PRIMARY KEY NOT NULL,
                valueInt INTEGER,
                valueString TEXT
            )"""
        )
    }
}
