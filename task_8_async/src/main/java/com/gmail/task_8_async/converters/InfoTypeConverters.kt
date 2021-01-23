package com.gmail.task_8_async.converters

import androidx.room.TypeConverter
import com.gmail.task_8_async.entity.InfoType

class InfoTypeConverters {

    @TypeConverter
    fun toInfoType(value: String) = getInfoType(value)

    @TypeConverter
    fun fromInfoType(value: InfoType) = value.type

    private fun getInfoType(type: String): InfoType {
        var infoType = InfoType.values().find { infoType -> infoType.type == type }
        if (infoType == null) {
            infoType = InfoType.PHONE_NUMBER
        }
        return infoType
    }
}