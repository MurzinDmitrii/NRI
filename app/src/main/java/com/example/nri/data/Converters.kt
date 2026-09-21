package com.example.nri.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromBagItemType(value: BagItemType): String = value.name

    @TypeConverter
    fun toBagItemType(value: String): BagItemType = BagItemType.valueOf(value)

    @TypeConverter
    fun fromWeaponType(value: WeaponType?): String? = value?.name

    @TypeConverter
    fun toWeaponType(value: String?): WeaponType? = value?.let { WeaponType.valueOf(it) }
}