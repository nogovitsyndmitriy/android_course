package com.gmail.task_8_weather_mvp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(indices = [Index(value = ["uid", "nameEn", "nameRu", "lat", "lon"], unique = true)])
data class City(
    @PrimaryKey
    val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "nameEn")
    var nameEn: String = "Minsk",
    @ColumnInfo(name = "nameRu")
    var nameRu: String = "Минск",
    @ColumnInfo(name = "lat")
    var lat: Double = 53.9,
    @ColumnInfo(name = "lon")
    var lon: Double = 27.5667,
    @ColumnInfo(name = "checked")
    var checked: Boolean = true
) : Serializable