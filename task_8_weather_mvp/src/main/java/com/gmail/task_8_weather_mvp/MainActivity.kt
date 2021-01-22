package com.gmail.task_8_weather_mvp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_weather_mvp.adapter.WeatherAdapter
import com.gmail.task_8_weather_mvp.presenter.HourlyPresenterImpl
import com.gmail.task_8_weather_mvp.presenter.viewmodel.WeatherViewModel
import com.gmail.task_8_weather_mvp.view.HourlyWeatherView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), HourlyWeatherView {

    private lateinit var hourlyPresenter: HourlyPresenterImpl
    private val weatherAdapter by lazy { WeatherAdapter() }
    private lateinit var recyclerView: RecyclerView
    private lateinit var cityName: TextView
    private lateinit var weatherInfo: TextView
    private lateinit var temperatureText: TextView
    private lateinit var settingsButton: ImageButton
    private lateinit var weatherIcon: ImageView
    private lateinit var selectCityButton: FloatingActionButton
    private lateinit var units: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        units = loadUnitStatus()
        hourlyPresenter = HourlyPresenterImpl(this, applicationContext as App)
        initViews()
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, 1)
        }
        selectCityButton.setOnClickListener {
            val intent = Intent(this, SelectCityActivity::class.java)
            startActivityForResult(intent, 2)
        }
        recyclerView.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }
        hourlyPresenter.getHourlyWeather()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            units = data.getStringExtra(UNITS).toString()
            hourlyPresenter.getHourlyWeather()
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            hourlyPresenter.getHourlyWeather()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.hourlyWeatherRecyclerView)
        temperatureText = findViewById(R.id.temperatureText)
        weatherInfo = findViewById(R.id.weatherInfo)
        settingsButton = findViewById(R.id.settingsButton)
        selectCityButton = findViewById(R.id.selectCityButton)
        cityName = findViewById(R.id.cityName)
        weatherIcon = findViewById(R.id.weatherIcon)
    }

    @SuppressLint("SetTextI18n")
    override fun fillPage(weatherViewModel: WeatherViewModel) {
        weatherAdapter.putAllWeatherList(weatherViewModel.hourly)
        if (loadUnitStatus() == METRIC) {
            temperatureText.text = weatherViewModel.currentTemp.toString() + "C"
        } else {
            temperatureText.text = weatherViewModel.currentTemp.toString() + "K"
        }
        if(weatherViewModel.currentTemp > 0 || weatherViewModel.currentTemp > 273.15){
            weatherIcon.setImageResource(R.drawable.ic_sunny)
        } else {
            weatherIcon.setImageResource(R.drawable.ic_cold)
        }
        weatherInfo.text = weatherViewModel.currentMainWeather
        cityName.text = weatherViewModel.currentCityName
    }

    private fun loadUnitStatus(): String {
        val sharedPrefs = applicationContext.getSharedPreferences(UNITS, Context.MODE_PRIVATE)
        return sharedPrefs.getString(UNITS, METRIC).toString()
    }
}