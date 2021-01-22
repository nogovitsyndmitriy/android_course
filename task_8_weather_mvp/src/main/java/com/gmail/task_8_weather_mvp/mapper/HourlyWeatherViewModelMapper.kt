package com.gmail.task_8_weather_mvp.mapper

import com.gmail.task_8_weather_mvp.data.HourlyWeatherResponse
import com.gmail.task_8_weather_mvp.presenter.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class HourlyWeatherViewModelMapper : (HourlyWeatherResponse) -> WeatherViewModel {

    override fun invoke(apiResponse: HourlyWeatherResponse): WeatherViewModel {

        val weatherViewModel = WeatherViewModel()
        val current = apiResponse.current
        val localDateTime = LocalDateTime.ofEpochSecond(
            current.dt.toLong(),
            0,
            ZoneOffset.ofTotalSeconds(apiResponse.timezoneOffset)
        )
        weatherViewModel.currentDate = localDateTime.hour
        weatherViewModel.currentClouds = current.clouds
        weatherViewModel.currentTemp = current.temp
        weatherViewModel.currentDescription = current.weather.first().description
        weatherViewModel.currentMainWeather = current.weather.first().main
        weatherViewModel.timezone = apiResponse.timezone
        weatherViewModel.timezoneOffset = apiResponse.timezoneOffset

        weatherViewModel.hourly = apiResponse.hourly.map {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ROOT)
            val date = Date(1000 * (it.dt.toLong() + apiResponse.timezoneOffset))
            WeatherViewModel.Hourly(
                clouds = it.clouds,
                dt = simpleDateFormat.format(date),
                temp = it.temp,
                weatherMain = it.weather.first().main
            )
        }
        return weatherViewModel
    }

}