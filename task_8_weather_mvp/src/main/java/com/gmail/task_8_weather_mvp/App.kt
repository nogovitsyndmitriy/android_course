package com.gmail.task_8_weather_mvp

import android.app.Application
import androidx.room.Room
import com.gmail.task_8_weather_mvp.data.WeatherApi
import com.gmail.task_8_weather_mvp.database.AppDatabase
import com.gmail.task_8_weather_mvp.entity.City
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var weatherApi: WeatherApi
    lateinit var dataBase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        configureDatabase()
        configureRetrofit()
    }

    private fun configureDatabase() {
        dataBase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "cities"
        )
            .allowMainThreadQueries()
            .build()
        if (dataBase.cityDAO().getAll().isEmpty()) {
            dataBase.cityDAO().saveAll(City())
        }
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)
    }
}