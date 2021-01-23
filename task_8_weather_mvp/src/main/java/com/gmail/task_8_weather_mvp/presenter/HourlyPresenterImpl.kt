package com.gmail.task_8_weather_mvp.presenter

import android.content.Context
import com.gmail.task_8_weather_mvp.*
import com.gmail.task_8_weather_mvp.data.HourlyWeatherResponse
import com.gmail.task_8_weather_mvp.data.WeatherApi
import com.gmail.task_8_weather_mvp.entity.City
import com.gmail.task_8_weather_mvp.mapper.DatabaseCityMapper
import com.gmail.task_8_weather_mvp.mapper.HourlyWeatherViewModelMapper
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel
import com.gmail.task_8_weather_mvp.presenter.viewmodel.WeatherViewModel
import com.gmail.task_8_weather_mvp.view.HourlyWeatherView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HourlyPresenterImpl(
    private var hourlyWeatherView: HourlyWeatherView,
    private var applicationContext: App,
    private val weatherApi: WeatherApi = applicationContext.weatherApi,
    private val weatherMapper: (HourlyWeatherResponse) -> WeatherViewModel = HourlyWeatherViewModelMapper()
) : HourlyPresenter {

    private val dataBase = applicationContext.dataBase
    private var currentCity = dataBase.cityDAO().getAll().find { city -> city.checked }

    override fun getHourlyWeather() {
        currentCity = dataBase.cityDAO().getAll().find { city -> city.checked }
        CoroutineScope(Dispatchers.Main).launch {
            hourlyWeatherView.fillPage(
                getWeatherHourlyResponse()
            )
        }
    }

    private suspend fun getWeatherHourlyResponse() =
        withContext(Dispatchers.IO) {
            val units = loadUnitStatus()
            val weather: WeatherViewModel = weatherMapper.invoke(weatherApi.getHourlyWeather(currentCity?.lat.toString(), currentCity?.lon.toString(), DAILY, units, API_KEY))
            weather.currentCityName = currentCity?.nameEn.toString()
            weather
        }

    private fun loadUnitStatus(): String {
        val sharedPrefs = applicationContext.getSharedPreferences(UNITS, Context.MODE_PRIVATE)
        return sharedPrefs.getString(UNITS, METRIC).toString()
    }
}