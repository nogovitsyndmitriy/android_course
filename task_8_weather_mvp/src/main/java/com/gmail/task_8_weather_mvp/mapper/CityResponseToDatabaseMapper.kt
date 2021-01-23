package com.gmail.task_8_weather_mvp.mapper

import com.gmail.task_8_weather_mvp.data.CityResponse
import com.gmail.task_8_weather_mvp.entity.City
import java.util.*

class CityResponseToDatabaseMapper : (CityResponse) -> City {

    override fun invoke(response: CityResponse): City {
        val cityData = response.first()
        return City(
            UUID.randomUUID().toString(),
            cityData.localNames.en,
            cityData.localNames.ru,
            cityData.lat,
            cityData.lon,
            checked = false
        )
    }
}