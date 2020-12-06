package com.gmail.task_8_async.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
class Contact(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null,
    @ColumnInfo(name = "isPhone")
    var isPhone: Boolean = true,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
) : Serializable