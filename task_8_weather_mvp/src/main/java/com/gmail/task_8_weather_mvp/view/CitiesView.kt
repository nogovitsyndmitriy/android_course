package com.gmail.task_8_weather_mvp.view

import com.gmail.task_8_weather_mvp.entity.City
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel

interface CitiesView {

    fun fillPage(cityViewModelList: MutableList<CityViewModel>)
    fun addNewCity(city: CityViewModel)
}