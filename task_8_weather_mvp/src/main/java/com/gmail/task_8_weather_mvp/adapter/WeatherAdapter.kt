package com.gmail.task_8_weather_mvp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_8_weather_mvp.R
import com.gmail.task_8_weather_mvp.presenter.viewmodel.WeatherViewModel

class WeatherAdapter :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private var list = listOf<WeatherViewModel.Hourly>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherViewHolder(inflater, parent)
    }

    fun putAllWeatherList(list: List<WeatherViewModel.Hourly>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val hourly: WeatherViewModel.Hourly = list[position]
        holder.bind(hourly)
    }

    class WeatherViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_weather_timing, parent, false)
    ) {
        private var weatherIconItem = itemView.findViewById<ImageView>(R.id.weatherIconItem)
        private var weatherTime = itemView.findViewById<TextView>(R.id.weatherTime)
        private var weather = itemView.findViewById<TextView>(R.id.weather)
        private var weatherTemperature = itemView.findViewById<TextView>(R.id.weatherTemperature)

        fun bind(hourly: WeatherViewModel.Hourly) {
            weatherTime.text = hourly.dt
            weather.text = hourly.weatherMain
            weatherTemperature.text = hourly.temp.toString()
        }
    }
}