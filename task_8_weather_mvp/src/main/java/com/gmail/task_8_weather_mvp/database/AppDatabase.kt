package com.gmail.task_8_weather_mvp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.task_8_weather_mvp.dao.CityDAO
import com.gmail.task_8_weather_mvp.entity.City

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDAO(): CityDAO
}
