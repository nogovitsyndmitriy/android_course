package com.gmail.task_8_weather_mvp.view

import com.gmail.task_8_weather_mvp.presenter.viewmodel.WeatherViewModel

interface HourlyWeatherView {
    fun fillPage(weatherViewModel: WeatherViewModel)
}