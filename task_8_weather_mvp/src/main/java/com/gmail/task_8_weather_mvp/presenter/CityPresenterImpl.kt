package com.gmail.task_8_weather_mvp.presenter

import com.gmail.task_8_weather_mvp.API_KEY
import com.gmail.task_8_weather_mvp.App
import com.gmail.task_8_weather_mvp.data.CityResponse
import com.gmail.task_8_weather_mvp.data.WeatherApi
import com.gmail.task_8_weather_mvp.entity.City
import com.gmail.task_8_weather_mvp.mapper.CityResponseToDatabaseMapper
import com.gmail.task_8_weather_mvp.mapper.DatabaseCityMapper
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel
import com.gmail.task_8_weather_mvp.view.CitiesView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityPresenterImpl(
    private val citiesView: CitiesView,
    private val applicationContext: App,
    private val weatherApi: WeatherApi = applicationContext.weatherApi,
    private val databaseCityMapper: (City) -> CityViewModel = DatabaseCityMapper(),
    private val cityResponseToDatabaseMapper: (CityResponse) -> City = CityResponseToDatabaseMapper()
) : CityPresenter {

    private val dataBase = applicationContext.dataBase

    override fun getCities() {
        CoroutineScope(Dispatchers.Main).launch {
            citiesView.fillPage(getCitiesFromDB())
        }
    }

    private suspend fun getCitiesFromDB(): MutableList<CityViewModel> = withContext(Dispatchers.IO) {
        dataBase.cityDAO().getAll().map { city -> databaseCityMapper.invoke(city) }.toMutableList()
    }

    override fun addCity(name: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val city = getCityByName(name)
            saveCity(city)
            citiesView.addNewCity(databaseCityMapper.invoke(city))
        }
    }

    private suspend fun getCityByName(name: String): City = withContext(Dispatchers.IO) {
        cityResponseToDatabaseMapper.invoke(weatherApi.getCityDataByName(name, API_KEY))
    }

    private suspend fun saveCity(city: City) = withContext(Dispatchers.IO) {
        dataBase.cityDAO().saveAll(city)
    }
}