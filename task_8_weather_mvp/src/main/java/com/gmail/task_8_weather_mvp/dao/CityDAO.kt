package com.gmail.task_8_weather_mvp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gmail.task_8_weather_mvp.entity.City

@Dao
interface CityDAO {

    @Insert
    fun saveAll(vararg cities: City)

    @Update
    fun update(city: City)

    @Query("SELECT * FROM city")
    fun getAll(): List<City>

    @Query("SELECT * FROM city WHERE uid = :userUid")
    fun findByUid(userUid: String): City

    @Query("DELETE FROM city WHERE uid = :uid")
    fun deleteById(uid: String)
}