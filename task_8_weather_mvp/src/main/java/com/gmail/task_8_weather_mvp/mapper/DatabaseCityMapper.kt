package com.gmail.task_8_weather_mvp.mapper

import com.gmail.task_8_weather_mvp.entity.City
import com.gmail.task_8_weather_mvp.presenter.viewmodel.CityViewModel

class DatabaseCityMapper : (City) -> CityViewModel {

    override fun invoke(city: City): CityViewModel {
        return CityViewModel(
            city.uid,
            city.lat,
            city.lon,
            en = city.nameEn,
            ru = city.nameRu,
            checked = city.checked
        )
    }
}