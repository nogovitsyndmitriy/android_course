package com.gmail.task_8_async.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gmail.task_8_async.R
import com.gmail.task_8_async.converters.InfoTypeConverters
import java.io.Serializable
import java.util.*

@Entity
class Contact(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "phone")
    var info: String? = null,
    @ColumnInfo(name = "infoType")
    var infoType: InfoType = InfoType.PHONE_NUMBER,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
) : Serializable

enum class InfoType(val type: String, val iconId: Int) {
    EMAIL("Email", R.drawable.ic_contact_email),
    PHONE_NUMBER("Phone number", R.drawable.ic_contact_phone),
}