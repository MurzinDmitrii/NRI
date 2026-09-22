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

    @TypeConverter
    fun fromCharacterInfoType(value: CharacterInfoType): String = value.name

    @TypeConverter
    fun toCharacterInfoType(value: String): CharacterInfoType = CharacterInfoType.valueOf(value)

    @TypeConverter
    fun fromCharacteristicType(value: CharacteristicType): String = value.name

    @TypeConverter
    fun toCharacteristicType(value: String): CharacteristicType = CharacteristicType.valueOf(value)

    @TypeConverter
    fun fromSubCharacteristicType(value: SubCharacteristicType): String = value.name

    @TypeConverter
    fun toSubCharacteristicType(value: String): SubCharacteristicType = SubCharacteristicType.valueOf(value)
}