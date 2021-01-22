package com.gmail.task_8_weather_mvp.presenter.viewmodel

class CityViewModel(
    val uid: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val ru: String = "",
    val en: String = "",
    var checked: Boolean = false
)