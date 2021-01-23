package com.gmail.task_8_weather_mvp.presenter.viewmodel

open class WeatherViewModel(
    var hourly: List<Hourly> = emptyList(),
    var currentCityName: String = "",
    var currentClouds: Int = 0,
    var currentDate: Int = 0,
    var currentTemp: Double = 0.0,
    var currentDescription: String = "",
    var currentMainWeather: String = "",
    var timezone: String = "",
    var timezoneOffset: Int = 0
) {
    class Hourly(
        var clouds: Int = 0,
        var dt: String = "",
        var temp: Double = 0.0,
        var weatherMain: String = ""
    )
}