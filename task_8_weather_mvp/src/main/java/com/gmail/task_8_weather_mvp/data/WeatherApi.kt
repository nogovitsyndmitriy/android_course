package com.gmail.task_8_weather_mvp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/geo/1.0/direct")
    suspend fun getCityDataByName(
        @Query("q") cityName: String,
        @Query("apiKey") apiKey: String
    ): CityResponse

    @GET("/data/2.5/onecall")
    suspend fun getHourlyWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): HourlyWeatherResponse
}